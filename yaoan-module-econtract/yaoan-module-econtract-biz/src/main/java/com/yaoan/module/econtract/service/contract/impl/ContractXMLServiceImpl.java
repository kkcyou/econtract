package com.yaoan.module.econtract.service.contract.impl;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.reach.platform.sdk.base.client.OpenClientApi;
import com.reach.platform.sdk.base.config.SdkConfigure;
import com.reach.platform.sdk.base.entity.base.R;
import com.reach.platform.sdk.base.entity.cert.ApiSignReq;
//import com.reach.platform.sdk.base.entity.cert.ApiVerifyReq;
import com.reach.platform.sdk.base.entity.cert.ApiVerifyReqV2;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.ContractVO;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.contract.ContractXMLService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.util.ContractXMPSchema;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.XmpSerializer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import com.yaoan.module.system.api.user.SupplyApi;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;
import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;
import static com.yaoan.module.econtract.service.contract.impl.ContractServiceImpl.createMultipartFile;
import static com.yaoan.module.econtract.service.contract.impl.ContractServiceImpl.downloadFileToTemp;

@Service
@Slf4j
public class ContractXMLServiceImpl implements ContractXMLService {
    @Resource
    private ContractOrderExtMapper contractExtMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private FileApi fileApi;

    @Override
    public String getContractXML(String id) {
        ContractOrderExtDO contractDO = contractExtMapper.selectById(id);
        if (ObjectUtils.isEmpty(contractDO)) {
            //合同拓展表不存在查主表
            return getInfoByContract(id);
//            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY,"合同不存在");
        }
        XmlMapper xmlMapper = new XmlMapper();
        ContractXMLVO vo = new ContractXMLVO();
        // 格式化 SignDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSignDate = "";
        if (ObjectUtils.isNotEmpty(contractDO.getContractSignTime())) {
            formattedSignDate = dateFormat.format(contractDO.getContractSignTime());
        }
        vo.setBasicInfo(new ContractXMLVO.BasicInfo().setContractID(contractDO.getId())
                .setName(contractDO.getName()).setSignDate(formattedSignDate)
                .setSignAddress(contractDO.getContractSignAddress()).setAmount(contractDO.getTotalMoney()).setCurrency("CNY"));
        ArrayList<ContractXMLVO.Party> objects = new ArrayList<>();
        ContractXMLVO.Party party = new ContractXMLVO.Party();
        party.setRole("partA");
        party.setName(contractDO.getBuyerOrgName());
        party.setAddress(contractDO.getDeliveryAddress());
        party.setContact(StringUtils.isNotEmpty(contractDO.getBuyerLink()) ? contractDO.getBuyerLink() : contractDO.getBuyerProxy());
        party.setProxy(contractDO.getBuyerProxy());
        party.setPhone(contractDO.getBuyerLinkMobile());
        ContractXMLVO.Party party1 = new ContractXMLVO.Party();
        party1.setRole("partB");
        party1.setName(contractDO.getSupplierName());
        party1.setAddress(contractDO.getRegisteredAddress());
        party1.setContact(StringUtils.isEmpty(contractDO.getSupplierLink()) ? contractDO.getSupplierProxy() : contractDO.getSupplierLink());
        party1.setProxy(contractDO.getSupplierProxy());
        party1.setPhone(contractDO.getSupplierLinkMobile());
        party1.setBankName(contractDO.getBankName());
        party1.setBankAccount(contractDO.getBankAccount());
        objects.add(party);
        objects.add(party1);
        vo.setParties(new ContractXMLVO.Parties().setPartyList(objects));
        //支付计划
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getContractId, id).orderByAsc(PaymentScheduleDO::getSort));
        List<ContractXMLVO.Phase> objects1 = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
            objects1 = PaymentScheduleConverter.INSTANCE.toXMLVOList(paymentScheduleDOList);
//            ContractXMLVO.Phase phase = new ContractXMLVO.Phase().setPhaseID("1").setDescription("签署合同并支付首付款").setDueDate("2025-03-01").setAmount("100000.00").setCurrency("CNY");
//        ContractXMLVO.Phase phase1 = new ContractXMLVO.Phase().setPhaseID("2").setDescription("交付第一批货物").setDueDate("2025-07-01").setAmount("100000.00").setCurrency("CNY");
//        ContractXMLVO.Phase phase2 = new ContractXMLVO.Phase().setPhaseID("2").setDescription("交付第一批货物").setDueDate("2025-12-01").setAmount("100000.00").setCurrency("CNY");
//        objects1.add(phase);
//        objects1.add(phase1);
//        objects1.add(phase2);
        }
        vo.setPerformancePlan(new ContractXMLVO.PerformancePlan().setPhaseList(objects1));
        vo.setSignature(new ContractXMLVO.Signature().setSignatureAlgorithm(""));
        try {
            String s = xmlMapper.writeValueAsString(vo);
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s);
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @DataPermission
    public Long saveContractXML(String id, String ContractId) {
        try {
            ContractDO contractDO = contractMapper.selectById(ContractId);
            FileDTO fileDTO = fileApi.selectById(id);
            File file = downloadFileToTemp(fileDTO);
            //1. 拼装需要加密的xml
            ContractEndXmlVO contractEndXmlVO = new ContractEndXmlVO();
            contractEndXmlVO.setId(contractDO.getId());
            contractEndXmlVO.setCode(contractDO.getCode());
            contractEndXmlVO.setName(contractDO.getName());
            contractEndXmlVO.setAmount(contractDO.getAmount());
            contractEndXmlVO.setPartAName(contractDO.getPartAName());
            contractEndXmlVO.setPartBName(contractDO.getPartBName());
            ArrayList<PaymentPlanXML> objects = new ArrayList<>();
            PaymentPlanXML paymentPlanXML = new PaymentPlanXML();
            paymentPlanXML.setId(contractDO.getId());
            objects.add(paymentPlanXML);
            contractEndXmlVO.setPaymentPlan(objects);
            // 创建 JAXBContext
            JAXBContext context = JAXBContext.newInstance(ContractEndXmlVO.class);

            // 创建 Marshaller
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // 将对象转换为 XML 字符串
            StringWriter writer = new StringWriter();
            marshaller.marshal(contractEndXmlVO, writer);
            // 获取 XML 字符串
            String xmlString = writer.toString();
            //2. 获取签名
            ApiSignReq apiSignReq = new ApiSignReq();
            //证书标识
            apiSignReq.setKeySn("08ce409c728943199870674b42c7acb1@SN");
            //签名类型 //1：P7签名 2 P1签名 3: P7_ATTACH签名
            apiSignReq.setSignType("1");
            //签名原文
            apiSignReq.setOriginText(Base64.encode(xmlString));
            String s = JSON.toJSONString(apiSignReq);
            System.out.println("签名请求:" + s);
            //全局只需初始化一次参数
            SdkConfigure.init("http://114.115.214.104:803/", "0dfda7d1d11845a6a53c32374c8b7b2c",
                    "ae5d60793a5845c19440a289e06d6008", "1479363604116406272");
            R call = OpenClientApi.createClient().configuration("CERT.SIGN").build(s).ready().call();
            if (200 == call.getCode()) {
                JSONObject data = (JSONObject) call.getData();
                System.out.println("签名响应:" + data.toJSONString());
                String signature = data.getString("signature");
                //3. 生成最终包含签名的xml
                DigitalSignatureXML digitalSignatureXML = new DigitalSignatureXML().setSignatureValue(signature);
                digitalSignatureXML.setSignedOn(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                contractEndXmlVO.setDigitalSignature(digitalSignatureXML);
                // 创建 JAXBContext
                JAXBContext endContext = JAXBContext.newInstance(ContractEndXmlVO.class);

                // 创建 Marshaller
                Marshaller endMarshaller = endContext.createMarshaller();
                endMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                // 将对象转换为 XML 字符串
                StringWriter endWriter = new StringWriter();
                endMarshaller.marshal(contractEndXmlVO, endWriter);
                // 获取 XML 字符串
                String endXmlString = endWriter.toString();
                //4. 将xml写入pdf中
                PDDocument document = PDDocument.load(file);

                // 获取 PDF 的 Catalog
                PDDocumentCatalog catalog = document.getDocumentCatalog();

                // 创建 XMP 元数据对象
                XMPMetadata metadata = XMPMetadata.createXMPMetadata();
                //  创建自定义 XMP Schema（使用 ContractXMPSchema）
                ContractXMPSchema customSchema = new ContractXMPSchema(metadata, "http://www.huiyueyun.com/contract/", "contract");
                String base64EncodedXml = java.util.Base64.getEncoder().encodeToString(endXmlString.getBytes());
                customSchema.setData("contractData", base64EncodedXml);

                // 添加 Schema
                metadata.addSchema(customSchema);

                //  序列化 XMP 数据
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                new XmpSerializer().serialize(metadata, baos, true);
                ByteArrayInputStream xmpInputStream = new ByteArrayInputStream(baos.toByteArray());

                //  PDMetadata
                PDMetadata pdMetadata = new PDMetadata(document, xmpInputStream);
                catalog.setMetadata(pdMetadata);

                // 5. 需临时保存PDF后删除  否则查询不到元数据
                //临时存入 上传后删除
//                File outputFile = new File(FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf");
                File outputFile = new File(READY_TO_UPLOAD_PATH + IdUtil.fastSimpleUUID() + ".pdf");
                document.save(outputFile);
                document.close();
                Long xmlPdf = fileApi.uploadFile(contractDO.getName() + "XML" + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", createMultipartFile(outputFile).getBytes());
                //删除临时文件
                try {
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                } catch (Exception e) {
                    System.out.println("删除临时文件异常:" + outputFile.getPath());
                }
                return xmlPdf;
            } else {
                throw exception(SYSTEM_ERROR, "签名失败!失败原因：" + call);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(SYSTEM_ERROR, "生成xml失败!失败原因：" + e.getMessage());
        }
    }

    /**
     * 根据合同 pdf获取xml 验证真实性
     *
     * @param
     * @return
     */
    @Override
    public Object validateContractXML(ValidatePdfVO vo) {
        String  xml = getContractXMLString(vo);
        try {
            // 1. 创建JAXBContext

            JAXBContext context = JAXBContext.newInstance(ContractVO.class);

            // 2. 创建Unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // 3. 执行转换
            ContractVO contract = (ContractVO) unmarshaller.unmarshal(new StringReader(xml));
            String signature = contract.getSecurityInformation().getDigitalCertificate();
            contract.setSecurityInformation(null);
            // 4. 使用转换后的对象转为xml加密比较

            // 创建 Marshaller
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // 将对象转换为 XML 字符串
            StringWriter writer = new StringWriter();
            marshaller.marshal(contract, writer);
            // 获取 XML 字符串
            String xmlString = writer.toString();
            ApiVerifyReqV2 apiSignReq = new ApiVerifyReqV2();
           return verify(signature, "1", Base64.encode(xmlString), "1", "d4fa9eff60ff4cfdbc7f06777b486568@SN",
                    apiSignReq.getCertBase64());
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(SYSTEM_ERROR, "验证xml失败!失败原因：" + e.getMessage());
        }
    }

    @Override
    public String getContractXMLString(ValidatePdfVO vo) {
        try {
            if (ObjectUtils.isNotEmpty(vo.getFileId())) {
                FileDTO fileDTO = fileApi.selectById(vo.getFileId());
                // 根据文件url，获取文件
                MultipartFile fileFromUrl = getFileFromUrl(fileDTO.getUrl());
                vo.setFile(fileFromUrl);
            }
            // 1. 读取 PDF
            PDDocument document = null;
            try (InputStream pdfStream = vo.getFile().getInputStream()) {
                document = PDDocument.load(pdfStream);
            }

            // 2. 获取 PDF 元数据
            PDMetadata metadata = document.getDocumentCatalog().getMetadata();
            if (metadata != null) {

                // 3. 读取 XMP 内容
                InputStream inputStream = metadata.createInputStream();
                String xmpContent = readXMPContent(inputStream);

                // 4. 解析 XMP 数据并提取合同信息
                String contractData = extractContractDataFromXMP(xmpContent);
                document.close();
                if (contractData != null) {
                    return contractData;
                } else {
                    log.error("获取xml失败!失败原因：未找到合同数据！");
                    throw exception(DIY_ERROR, "请联系管理员。\"");
                }
            } else {
                log.error("获取xml失败!失败原因：没有找到 XMP 元数据！");
                throw exception(DIY_ERROR, "请联系管理员。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(DIY_ERROR, "获取xml失败!失败原因：" + e.getMessage());
        }
    }

    public static MultipartFile getFileFromUrl(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();

        // 获取文件名
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        // 获取文件类型
        String contentType = connection.getContentType();

        try (InputStream inputStream = connection.getInputStream()) {
            // 创建MultipartFile对象
            return new MockMultipartFile(
                    fileName,             // 文件名
                    fileName,            // 原始文件名
                    contentType,         // 内容类型
                    inputStream          // 文件内容流
            );
        }
    }

    // 读取 XMP 内容到字符串
    private static String readXMPContent(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return new String(outputStream.toByteArray(), "UTF-8");
    }

    // 从 XMP 内容中提取合同数据并进行 Base64 解码
    private static String extractContractDataFromXMP(String xmpContent) {
        try {
            // 解析 XMP 内容并找到 contract:contractData
            String base64EncodedXml = extractBase64ContractData(xmpContent);
            if (base64EncodedXml != null) {
                // Base64 解码
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64EncodedXml);
                return new String(decodedBytes, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw  exception(DIY_ERROR, "获取xml失败!失败原因：解析XMP失败！");
        }
        return null;
    }

    // 使用正则表达式从 XMP 内容中提取 contractData 的 Base64 编码
    private static String extractBase64ContractData(String xmpContent) {
        String regex = "(?:<contract:contractData>(.*?)</contract:contractData>|contract:contractData=\\\"(.*?)\\\")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xmpContent);
        if (matcher.find()) {
            return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);  // 返回匹配到的 Base64 编码的 XML 字符串
        }
        return null;
    }

    private String getInfoByContract(String id) {
        ContractDO contractDO = contractMapper.selectById(id);
        if (ObjectUtils.isEmpty(contractDO)) {
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "合同不存在");
        }
        XmlMapper xmlMapper = new XmlMapper();
        ContractXMLVO vo = new ContractXMLVO();
        // 格式化 SignDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSignDate = "";
        if (ObjectUtils.isNotEmpty(contractDO.getSignDate())) {
            formattedSignDate = dateFormat.format(contractDO.getSignDate());
        } else if (ObjectUtils.isNotEmpty(contractDO.getContractSignTime())) {
            formattedSignDate = dateFormat.format(contractDO.getContractSignTime());
        }
        vo.setBasicInfo(new ContractXMLVO.BasicInfo().setContractID(contractDO.getId())
                .setName(contractDO.getName()).setSignDate(formattedSignDate)
                .setSignAddress(contractDO.getLocation()).setAmount((new BigDecimal(contractDO.getAmount().toString()))).setCurrency("CNY"));
        ArrayList<ContractXMLVO.Party> objects = new ArrayList<>();
        ContractXMLVO.Party party = new ContractXMLVO.Party();
        party.setRole("partA");
        party.setName(contractDO.getPartAName());
        party.setAddress(contractDO.getDeliveryAddress());
        party.setContact(StringUtils.isNotEmpty(contractDO.getBuyerLink()) ? contractDO.getBuyerLink() : contractDO.getBuyerProxy());
        party.setProxy(contractDO.getBuyerProxy());
        party.setPhone(contractDO.getBuyerLinkMobile());
        OrganizationDTO organization = organizationApi.getOrganization(contractDO.getBuyerOrgId());
        if (ObjectUtils.isNotEmpty(organization)) {
            party.setName(StringUtils.isNotEmpty(party.getName()) ? party.getName() : organization.getName());
            party.setAddress(StringUtils.isNotEmpty(party.getAddress()) ? party.getAddress() : organization.getAddress());
            party.setContact(StringUtils.isNotEmpty(party.getContact()) ? party.getContact() : organization.getLinkMan());
            party.setProxy(StringUtils.isNotEmpty(party.getProxy()) ? party.getProxy() : organization.getLegal());
            party.setPhone(StringUtils.isNotEmpty(party.getPhone()) ? party.getPhone() : organization.getLinkPhone());
        }
        ContractXMLVO.Party party1 = new ContractXMLVO.Party();
        party1.setRole("partB");
        party1.setName(contractDO.getPartBName());
        party1.setAddress(contractDO.getRegisteredAddress());
        party1.setContact(StringUtils.isEmpty(contractDO.getSupplierLink()) ? contractDO.getSupplierProxy() : contractDO.getSupplierLink());
        party1.setProxy(contractDO.getSupplierProxy());
        party1.setPhone(contractDO.getSupplierLinkMobile());
        party1.setBankName(contractDO.getBankName());
        party1.setBankAccount(contractDO.getBankAccount());
        SupplyDTO supply = supplyApi.getSupply(contractDO.getSupplierId());
        if (ObjectUtil.isNull(supply)) {
            supply = hljSupplyService.getSupply(contractDO.getSupplierId());
        }
        if (ObjectUtils.isNotEmpty(supply)) {
            party1.setName(StringUtils.isNotEmpty(party.getName()) ? party.getName() : supply.getSupplyCn());
            party1.setAddress(StringUtils.isNotEmpty(party.getAddress()) ? party.getAddress() : supply.getAddr());
            party1.setContact(StringUtils.isNotEmpty(party.getContact()) ? party.getContact() : supply.getPersonName());
            party1.setProxy(StringUtils.isNotEmpty(party.getProxy()) ? party.getProxy() : supply.getLegalPerson());
            party1.setPhone(StringUtils.isNotEmpty(party.getPhone()) ? party.getPhone() : supply.getPersonMobile());
            party1.setBankName(StringUtils.isNotEmpty(party.getBankName()) ? party.getBankName() : supply.getBankName());
            party1.setBankAccount(StringUtils.isNotEmpty(party.getBankAccount()) ? party.getBankAccount() : supply.getBankAccount());
        }
        objects.add(party);
        objects.add(party1);
        vo.setParties(new ContractXMLVO.Parties().setPartyList(objects));
        //支付计划
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getContractId, id).orderByAsc(PaymentScheduleDO::getSort));
        List<ContractXMLVO.Phase> objects1 = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
            objects1 = PaymentScheduleConverter.INSTANCE.toXMLVOList(paymentScheduleDOList);
//            ContractXMLVO.Phase phase = new ContractXMLVO.Phase().setPhaseID("1").setDescription("签署合同并支付首付款").setDueDate("2025-03-01").setAmount("100000.00").setCurrency("CNY");
//        ContractXMLVO.Phase phase1 = new ContractXMLVO.Phase().setPhaseID("2").setDescription("交付第一批货物").setDueDate("2025-07-01").setAmount("100000.00").setCurrency("CNY");
//        ContractXMLVO.Phase phase2 = new ContractXMLVO.Phase().setPhaseID("2").setDescription("交付第一批货物").setDueDate("2025-12-01").setAmount("100000.00").setCurrency("CNY");
//        objects1.add(phase);
//        objects1.add(phase1);
//        objects1.add(phase2);
        }
        vo.setPerformancePlan(new ContractXMLVO.PerformancePlan().setPhaseList(objects1));
        vo.setSignature(new ContractXMLVO.Signature().setSignatureAlgorithm(""));
        try {
            String s = xmlMapper.writeValueAsString(vo);
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s);
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验签
     * @param signedData
     * @param signType
     * @param originText
     * @param signDataType
     * @param keySn
     * @param certBase64
     */
    public String  verify(String signedData, String signType, String originText, String signDataType, String keySn, String certBase64) {
        String url = "http://114.115.214.104:803/";
        String appId = "0dfda7d1d11845a6a53c32374c8b7b2c";
        String appSecret = "ae5d60793a5845c19440a289e06d6008";
        String projectKey = "1479363604116406272";
        //全局只需初始化一次参数
        SdkConfigure.init(url, appId, appSecret, projectKey);
        ApiVerifyReqV2 verifyReq = new ApiVerifyReqV2();
        verifyReq.setKeySn(keySn);
        verifyReq.setSignType(signType);
        verifyReq.setOriginText(originText);
        verifyReq.setSignDataType(signDataType);
        verifyReq.setSignedText(signedData);
        if ("3".equals(signType)) {
            verifyReq.setOriginText(null);
            verifyReq.setSignDataType(null);

        }
        if ("1".equals(signType)) {
            verifyReq.setKeySn(null);
        }
        verifyReq.setCertBase64(certBase64);
        String s = JSON.toJSONString(verifyReq);
        System.out.println("验签请求:" + s);

        R call = OpenClientApi.createClient().configuration("CERT.VERIFY").build(s).ready().call();
        System.out.println("验签响应" + JSON.toJSONString(call, true));
        if (200 == call.getCode()) {
            JSONObject data = (JSONObject) call.getData();
            System.out.println("签名响应:" + data.toJSONString());
            String signature = data.getString("certBase64");
            if("true".equals(((JSONObject) call.getData()).getString("verify"))){
                return "验签成功----"+((JSONObject) call.getData()).getString("verify");
            }else {
                return "验签失败---签名不一致";
            }
        }else {
            return "验签失败---" + call.getMessage();
        }
    }
}
