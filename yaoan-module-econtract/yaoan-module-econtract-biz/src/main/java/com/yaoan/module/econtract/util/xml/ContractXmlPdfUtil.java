package com.yaoan.module.econtract.util.xml;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reach.platform.sdk.base.client.OpenClientApi;
import com.reach.platform.sdk.base.config.SdkConfigure;
import com.reach.platform.sdk.base.entity.base.R;
import com.reach.platform.sdk.base.entity.cert.ApiSignReq;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.contract.dto.purchasing.ContractBillStatisticalDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractBillVo;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.PaymentPlanDTO;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.*;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractParamFieldConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractGoodsConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractPaymentPlayConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.enums.neimeng.AttachmentTypeEnums;
import com.yaoan.module.econtract.util.ContractXMPSchema;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.dept.MajorEnum;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.XmpSerializer;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

public class ContractXmlPdfUtil {

    public static File generateSignedPdf(
            ContractDO contractDO,ContractOrderExtDO contractExtDO, TradingContractExtDO orderContractDO, SupplyDTO supply,
            List<ContractBillVo> contractBillVOS, List<PaymentPlanDTO> paymentPlanDTOS, List<OrderContractParamFieldDO> orderContractParamFieldDOS, File pdfFile,
            List<ContractFileDO> contractFileDOS, List<CompanyRespDTO> companyByIds, List<Relative> relatives, List<TradingSupplierDO> tradingSupplierDOS) throws PdfProcessingException {
        try {
            // 1. 生成合同XML结构
            ContractVO xmlData = buildContractXml(contractDO,contractExtDO, orderContractDO, supply, contractBillVOS, paymentPlanDTOS, orderContractParamFieldDOS, pdfFile, contractFileDOS,companyByIds,relatives, tradingSupplierDOS);
            ContractVOCleanerUtil.filterNullAndEmptyFields(xmlData);
            // 2. XML签名处理流程
            SignConfig config = new SignConfig();
            config.setApiUrl("http://114.115.214.104:803/");
            config.setAppId("0dfda7d1d11845a6a53c32374c8b7b2c");
            config.setAppSecret("ae5d60793a5845c19440a289e06d6008");
            config.setTenantId("1479363604116406272");
            config.setKeySn("d4fa9eff60ff4cfdbc7f06777b486568@SN");
            String signedXml = signXmlContent(xmlData, config);

            // 3. 将签名XML嵌入PDF
            return embedXmlToPdf(pdfFile, signedXml, config.getTempDir());
        } catch (JAXBException e) {
            throw new PdfProcessingException("XML处理失败", e);
        } catch (IOException e) {
            throw new PdfProcessingException("PDF文件操作失败", e);
        } catch (SignatureException e) {
            throw new PdfProcessingException("数字签名失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ContractVO buildContractXml(ContractDO contractDO,ContractOrderExtDO orderContractDO, TradingContractExtDO contractExtDO, SupplyDTO supply,
                                               List<ContractBillVo> contractBillVOS, List<PaymentPlanDTO> paymentPlanDTOS, List<OrderContractParamFieldDO> orderContractParamFieldDOS,
                                               File pdfFile, List<ContractFileDO> contractFileDOS, List<CompanyRespDTO> companyByIds, List<Relative> relatives,List<TradingSupplierDO> tradingSupplierDOS) {
        ContractVO xml = new ContractVO();
        // 政采
        if (ObjectUtil.isNotEmpty(orderContractDO)) {
            xml = getContractVO(orderContractDO, contractExtDO, supply, contractBillVOS, paymentPlanDTOS, orderContractParamFieldDOS, pdfFile, contractFileDOS, xml,tradingSupplierDOS);
        // 非政采
        } else {
            // 通用信息
            GeneralInformation generalInformation = ContractConverter.INSTANCE.convertGeneralInformationV2(contractDO);
            xml.setGeneralInformation(generalInformation);
            // 当事人信息列表
            List<PartyInformation> partyInformations = new ArrayList<>();
            // 采购人信息
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            PartyInformation orgPartyInformation = new PartyInformation();
            if (ObjectUtil.isNotEmpty(companyByIds)) {
                companyByIds.forEach(companyRespDTO -> {
                    if (MajorEnum.PERSONAL.getMajor().equals(companyRespDTO.getMajor())) {
                        orgPartyInformation.setPartyType("自然人");
                    } else {
                        orgPartyInformation.setPartyType("机构");
                    }
                });
            }
            orgPartyInformation.setPartyGuid(String.valueOf(loginUser.getCompanyId()));
            // 采购人机构信息
            OrganizationInfo organizationInfo = new OrganizationInfo();
            organizationInfo.setOrgName(contractDO.getPartAName());
            organizationInfo.setLegalRepresent(loginUser.getNickName());
            organizationInfo.setPrincipal(loginUser.getNickName());
//            organizationInfo.setAgent(orderContractDO.getBuyerProxy());
//            organizationInfo.setRegion(orderContractDO.getRegionName());
//            organizationInfo.setRegionCode(orderContractDO.getRegionCode());
            if (CollectionUtil.isNotEmpty(companyByIds)) {
                organizationInfo.setOrgCode(companyByIds.get(0) != null ? companyByIds.get(0).getCreditCode() : null);
            }
            organizationInfo.setDomicile(contractDO.getLocation());
            orgPartyInformation.setOrganizationInfo(organizationInfo);
            // 采购人联系信息
            ContactInformation contactInfo = new ContactInformation();
            contactInfo.setContactName(loginUser.getNickName());
            if (ObjectUtil.isNotEmpty(companyByIds)) {
                companyByIds.forEach(companyRespDTO -> {
                    contactInfo.setTelephoneNumber(companyRespDTO.getPhone());
                    contactInfo.setEmail(companyRespDTO.getEmail());
                });
            }
//            contactInfo.setMailingAddress(contractExtDO.getOrgAddress());
//            contactInfo.setPostalCode(orderContractDO.getOrgPostalCode());
            orgPartyInformation.setContactInfo(contactInfo);
            // 采购人银行账户信息
//            BankAccountInformation orgBankAccountInfo = new BankAccountInformation();
//            orgBankAccountInfo.setBankAccountName(orderContractDO.getOrgBankAccountName());
//            orgBankAccountInfo.setBankName(orderContractDO.getOrgBankName());
//            orgBankAccountInfo.setBankAccountNumber(orderContractDO.getOrgBankAccount());
//            orgPartyInformation.setAccountInfo(orgBankAccountInfo);
            partyInformations.add(orgPartyInformation);
            // 相对方信息
            if (CollectionUtil.isNotEmpty(relatives)) {
                relatives.forEach(relative -> {
                    PartyInformation supPartyInformation = new PartyInformation();
                    if (MajorEnum.PERSONAL.getMajor().equals(Integer.valueOf(relative.getEntityType()))) {
                        supPartyInformation.setPartyType("自然人");
                        PersionInformation personInfo = new PersionInformation();
                        personInfo.setPersonName(relative.getName());
                        personInfo.setCertificateType(String.valueOf(relative.getCardType()));
                        personInfo.setCertificateNumber(relative.getCardNo());
                        personInfo.setResidentialAddress(relative.getAddress());
                        supPartyInformation.setPersonInfo(personInfo);
                    }  else {
                        supPartyInformation.setPartyType("机构");
                        // 相对方机构信息
                        OrganizationInfo supInfo = new OrganizationInfo();
                        supInfo.setOrgName(relative.getName());
                        supInfo.setLegalRepresent(relative.getLegalName());
                        supInfo.setPrincipal(relative.getHeadName());
                        supInfo.setAgent(relative.getContactName());
                        supInfo.setRegion(relative.getArea());
                        supInfo.setRegionCode(relative.getArea());
                        supInfo.setDomicile(relative.getAddress());
                        supInfo.setUnifiedSocialCreditCode(relative.getCardNo());
//                    supInfo.setSupplierSize(String.valueOf(orderContractDO.getSupplierSize()));
//                    supInfo.setSupplierFeatures(String.valueOf(orderContractDO.getSupplierFeatures()));
//                    supInfo.setForeignInvestmentType(String.valueOf(orderContractDO.getForeignInvestmentType()));
//                    supInfo.setCountryType(orderContractDO.getCountryType());
//                    supInfo.setTotalMoney(orderContractDO.getTotalMoney());
                        supPartyInformation.setOrganizationInfo(supInfo);
                    }
                    supPartyInformation.setPartyGuid(relative.getId());
                    // supPartyInformation.setPartyCharacter("");当事人角色：0要约人、1受要约人、2第三人等

                    // 相对方联系信息
                    ContactInformation supContactInfo = new ContactInformation();
                    supContactInfo.setContactName(relative.getContactName());
                    supContactInfo.setTelephoneNumber(relative.getContactTel());
                    supContactInfo.setEmail(relative.getEmail());
                    supContactInfo.setMailingAddress(relative.getAddress());
//                    supContactInfo.setPostalCode(orderContractDO.getSupplierPostalCode());
                    supPartyInformation.setContactInfo(supContactInfo);
                    // 相对方银行账户信息
                    BankAccountInformation supBankAccountInfo = new BankAccountInformation();
                    supBankAccountInfo.setBankAccountName(relative.getBankAccountName());
                    supBankAccountInfo.setBankName(relative.getBankName());
                    supBankAccountInfo.setBankAccountNumber(relative.getBankAccount());
//                    supBankAccountInfo.setBankNO(orderContractDO.getBankNo());
                    supPartyInformation.setAccountInfo(supBankAccountInfo);
                    partyInformations.add(supPartyInformation);
                });
            }
            ContractVO.PartyInformations partyInformations1 = new ContractVO.PartyInformations();
            partyInformations1.setPartyInformation(partyInformations);
            xml.setPartyInformations(partyInformations1);
            // 标的信息列表
            List<ContractObjectInformation> contractObjectInformations = ContractGoodsConverter.INSTANCE.toContractObjectInformations(contractBillVOS);
            ContractVO.ContractObjectInformations contractObjectInformations1 = new ContractVO.ContractObjectInformations();
            contractObjectInformations1.setContractObjectInformation(contractObjectInformations);
            xml.setContractObjectInformations(contractObjectInformations1);
            // 支付信息列表
            List<PaymentInformation> paymentInformations = ContractPaymentPlayConverter.INSTANCE.toPaymentInformation(paymentPlanDTOS);
            ContractVO.PaymentInformations paymentInformations1 = new ContractVO.PaymentInformations();
            paymentInformations1.setPaymentInformation(paymentInformations);
            xml.setPaymentInformations(paymentInformations1);
            // 履约验收要求信息列表
            List<AcceptanceInformation> acceptanceInformations = ContractParamFieldConverter.INSTANCE.contractAcceptanceInformation(orderContractParamFieldDOS);
            ContractVO.AcceptanceInformations acceptanceInformations1 = new ContractVO.AcceptanceInformations();
            acceptanceInformations1.setAcceptanceInformation(acceptanceInformations);
            xml.setAcceptanceInformations(acceptanceInformations1);
            // 附件信息列表
            List<AttachmentInformation> attachmentInformations = new ArrayList<>();
            AttachmentInformation contractAttachmentVo = new AttachmentInformation();
            contractAttachmentVo.setAttachmentType(AttachmentTypeEnums.MAIN.getCode());
            contractAttachmentVo.setFileName(pdfFile.getName());
            contractAttachmentVo.setFilePath(pdfFile.getPath());
            attachmentInformations.add(contractAttachmentVo);
            if (CollectionUtil.isNotEmpty(contractFileDOS)) {
                contractFileDOS.forEach(fileDTO -> {
                    AttachmentInformation contractAttachmentVo1 = new AttachmentInformation().setAttachmentType(AttachmentTypeEnums.OTHER.getCode()).setFileName(fileDTO.getFileName()).setFilePath(fileDTO.getFileUrl());
                    attachmentInformations.add(contractAttachmentVo1);
                });
            }
            ContractVO.AttachmentInformations attachmentInformations1 = new ContractVO.AttachmentInformations();
            attachmentInformations1.setAttachmentInformation(attachmentInformations);
            xml.setAttachmentInformations(attachmentInformations1);
        }
        return xml;
    }

    private static ContractVO getContractVO(ContractOrderExtDO orderContractDO, TradingContractExtDO contractExtDO, SupplyDTO supply, List<ContractBillVo> contractBillVOS, List<PaymentPlanDTO> paymentPlanDTOS, List<OrderContractParamFieldDO> orderContractParamFieldDOS, File pdfFile, List<ContractFileDO> contractFileDOS, ContractVO xml, List<TradingSupplierDO> tradingSupplierDOS) {
        // 通用信息
        GeneralInformation generalInformation = ContractConverter.INSTANCE.convertGeneralInformation(orderContractDO);
        generalInformation.setAllowMortgage(orderContractDO.getAllowMortgage() == null ? null : !"0".equals(orderContractDO.getAllowMortgage()));
        xml.setGeneralInformation(generalInformation);
        // 当事人信息列表
        List<PartyInformation> partyInformations = new ArrayList<>();
        // 采购人信息
        PartyInformation orgPartyInformation = new PartyInformation();
//        orgPartyInformation.setPartyType("机构");
        orgPartyInformation.setPartyGuid(orderContractDO.getBuyerOrgId());
        // orgPartyInformation.setPartyCharacter("");当事人角色：0要约人、1受要约人、2第三人等
        // 采购人机构信息
        OrganizationInfo organizationInfo = new OrganizationInfo();
        organizationInfo.setOrgName(orderContractDO.getBuyerOrgName());
        organizationInfo.setLegalRepresent(orderContractDO.getBuyerLegalPerson());
        organizationInfo.setPrincipal(contractExtDO.getBuyerAgent());
        organizationInfo.setAgent(orderContractDO.getBuyerProxy());
        organizationInfo.setRegion(orderContractDO.getRegionName());
        organizationInfo.setRegionCode(orderContractDO.getRegionCode());
        organizationInfo.setOrgCode(orderContractDO.getOrgCode());
        organizationInfo.setDomicile(contractExtDO.getOrgAddress());
        orgPartyInformation.setOrganizationInfo(organizationInfo);
        // 采购人联系信息
        ContactInformation contactInfo = new ContactInformation();
        contactInfo.setContactName(orderContractDO.getBuyerLink());
        contactInfo.setTelephoneNumber(contractExtDO.getBuyerPhone());
        contactInfo.setEmail(orderContractDO.getBuyerEmail());
        contactInfo.setMailingAddress(contractExtDO.getOrgAddress());
        contactInfo.setPostalCode(orderContractDO.getOrgPostalCode());
        orgPartyInformation.setContactInfo(contactInfo);
        // 采购人银行账户信息
        BankAccountInformation orgBankAccountInfo = new BankAccountInformation();
        orgBankAccountInfo.setBankAccountName(orderContractDO.getOrgBankAccountName());
        orgBankAccountInfo.setBankName(orderContractDO.getOrgBankName());
        orgBankAccountInfo.setBankAccountNumber(orderContractDO.getOrgBankAccount());
        orgPartyInformation.setAccountInfo(orgBankAccountInfo);
        // 供应商信息
        PartyInformation supPartyInformation = new PartyInformation();
//        supPartyInformation.setPartyType("供应商");
        supPartyInformation.setPartyGuid(orderContractDO.getSupplierId());
        // supPartyInformation.setPartyCharacter("");当事人角色：0要约人、1受要约人、2第三人等
        // 供应商机构信息
        OrganizationInfo supInfo = new OrganizationInfo();
        supInfo.setOrgName(orderContractDO.getSupplierName());
        supInfo.setLegalRepresent(orderContractDO.getSupplierProxy());
        supInfo.setPrincipal(orderContractDO.getSupplierProxy());
        supInfo.setAgent(orderContractDO.getSupplierProxy());
        supInfo.setRegion(orderContractDO.getSupplierLocation());
        supInfo.setRegionCode(orderContractDO.getSupplierLocationCode());
        supInfo.setDomicile(orderContractDO.getRegisteredAddress());
        supInfo.setSupplierSize((orderContractDO.getSupplierSize() != null ? String.valueOf(orderContractDO.getSupplierSize()) : null));
        supInfo.setSupplierFeatures(orderContractDO.getSupplierFeatures() != null ? String.valueOf(orderContractDO.getSupplierFeatures()) : null);
        supInfo.setForeignInvestmentType(orderContractDO.getForeignInvestmentType() != null ? String.valueOf(orderContractDO.getForeignInvestmentType()) :  null);
        supInfo.setCountryType(orderContractDO.getCountryType());
        supInfo.setTotalMoney(orderContractDO.getTotalMoney());
        if (ObjectUtil.isNotEmpty(supply)) {
            supInfo.setUnifiedSocialCreditCode(supply.getOrgCode());
        }
        supPartyInformation.setOrganizationInfo(supInfo);
        // 供应商联系信息
        ContactInformation supContactInfo = new ContactInformation();
        supContactInfo.setContactName(orderContractDO.getSupplierLink());
        supContactInfo.setTelephoneNumber(orderContractDO.getSupplierLinkMobile());
        supContactInfo.setEmail(orderContractDO.getSupplierEmail());
        supContactInfo.setMailingAddress(orderContractDO.getRegisteredAddress());
        supContactInfo.setPostalCode(orderContractDO.getSupplierPostalCode());
        supPartyInformation.setContactInfo(supContactInfo);
        // 供应商银行账户信息
        BankAccountInformation supBankAccountInfo = new BankAccountInformation();
        if (CollectionUtil.isNotEmpty(tradingSupplierDOS)) {
            TradingSupplierDO tradingSupplierDO = tradingSupplierDOS.get(0);
            supBankAccountInfo.setBankAccountName(tradingSupplierDO.getPayPlanbAccount());
            supBankAccountInfo.setBankName(tradingSupplierDO.getBankName());
            supBankAccountInfo.setBankAccountNumber(tradingSupplierDO.getBankAccount());
            supBankAccountInfo.setBankNO(orderContractDO.getBankNo());
        }
        supPartyInformation.setAccountInfo(supBankAccountInfo);
        partyInformations.add(orgPartyInformation);
        partyInformations.add(supPartyInformation);
        ContractVO.PartyInformations partyInformations1 = new ContractVO.PartyInformations();
        partyInformations1.setPartyInformation(partyInformations);
        xml.setPartyInformations(partyInformations1);
        // 标的信息列表
        List<ContractObjectInformation> contractObjectInformations = ContractGoodsConverter.INSTANCE.toContractObjectInformations(contractBillVOS);
        if (CollectionUtil.isNotEmpty(contractObjectInformations)) {
            ContractVO.ContractObjectInformations contractObjectInformations1 = new ContractVO.ContractObjectInformations();
            contractObjectInformations1.setContractObjectInformation(contractObjectInformations);
            xml.setContractObjectInformations(contractObjectInformations1);
        }
        // 支付信息列表
        List<PaymentInformation> paymentInformations = ContractPaymentPlayConverter.INSTANCE.toPaymentInformation(paymentPlanDTOS);
        if (CollectionUtil.isNotEmpty(paymentInformations)) {
            ContractVO.PaymentInformations paymentInformations1 = new ContractVO.PaymentInformations();
            paymentInformations1.setPaymentInformation(paymentInformations);
            xml.setPaymentInformations(paymentInformations1);
        }
        // 履约验收要求信息列表
        List<AcceptanceInformation> acceptanceInformations = ContractParamFieldConverter.INSTANCE.contractAcceptanceInformation(orderContractParamFieldDOS);
        if (CollectionUtil.isNotEmpty(acceptanceInformations)) {
            ContractVO.AcceptanceInformations acceptanceInformations1 = new ContractVO.AcceptanceInformations();
            acceptanceInformations1.setAcceptanceInformation(acceptanceInformations);
            xml.setAcceptanceInformations(acceptanceInformations1);
        }
        // 附件信息列表
        List<AttachmentInformation> attachmentInformations = new ArrayList<>();
        AttachmentInformation contractAttachmentVo = new AttachmentInformation();
        contractAttachmentVo.setAttachmentType(AttachmentTypeEnums.MAIN.getCode());
        contractAttachmentVo.setFileName(pdfFile.getName());
        contractAttachmentVo.setFilePath(pdfFile.getPath());
        attachmentInformations.add(contractAttachmentVo);
        if (CollectionUtil.isNotEmpty(contractFileDOS)) {
            contractFileDOS.forEach(fileDTO -> {
                AttachmentInformation contractAttachmentVo1 = new AttachmentInformation().setAttachmentType(AttachmentTypeEnums.OTHER.getCode()).setFileName(fileDTO.getFileName()).setFilePath(fileDTO.getFileUrl());
                attachmentInformations.add(contractAttachmentVo1);
            });
        }
        ContractVO.AttachmentInformations attachmentInformations1 = new ContractVO.AttachmentInformations();
        attachmentInformations1.setAttachmentInformation(attachmentInformations);
        xml.setAttachmentInformations(attachmentInformations1);
        return xml;
    }

    private static String signXmlContent(ContractVO xml, SignConfig config) throws Exception {

        // 生成初始XML
        String xmlContent = convertToXml(xml);

        // 调用签名服务
        ApiSignReq request = new ApiSignReq();
        request.setKeySn(config.getKeySn());
        request.setSignType("1");
        request.setOriginText(cn.hutool.core.codec.Base64.encode(xmlContent));

        R call = callSignService(request, config);
        if (200 == call.getCode()) {
            JSONObject data = (JSONObject) call.getData();
            System.out.println("签名响应:" + data.toJSONString());
            // 添加签名信息
            SecurityInformation security = new SecurityInformation();
            security.setEncryptedData(cn.hutool.core.codec.Base64.encode(xmlContent));
            security.setDigitalCertificate(data.getString("signature"));
            security.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            xml.setSecurityInformation(security);
        } else {
            throw exception(SYSTEM_ERROR, "签名失败!失败原因：" + call);
        }
        return convertToXml(xml);
    }

    private static R callSignService(ApiSignReq request, SignConfig config) throws SignatureException {

        try {
            SdkConfigure.init(config.getApiUrl(), config.getAppId(), config.getAppSecret(), config.getTenantId());
            String s = JSON.toJSONString(request);
            System.out.println("签名请求:" + s);
            R call = OpenClientApi.createClient().configuration("CERT.SIGN").build(s).ready().call();

            if (call.getCode() != 200) {
                throw new SignatureException("签名服务错误: " + call.getMessage());
            }
            return call;
        } catch (Exception e) {
            throw new SignatureException("签名调用失败", e);
        }
    }

    private static File embedXmlToPdf(File pdfFile, String xmlContent, String tempDir) throws IOException {

        try (PDDocument doc = PDDocument.load(pdfFile)) {
            // 创建XMP元数据
            XMPMetadata metadata = XMPMetadata.createXMPMetadata();
            ContractXMPSchema schema = new ContractXMPSchema(metadata, "http://www.huiyueyun.com/contract/", "contract");
            String encodedXml = Base64.getEncoder().encodeToString(xmlContent.getBytes());
            schema.setData("contractData", encodedXml);
            metadata.addSchema(schema);

            // 序列化元数据
            ByteArrayOutputStream xmpStream = new ByteArrayOutputStream();
            new XmpSerializer().serialize(metadata, xmpStream, true);

            // 更新PDF元数据
            PDMetadata pdMeta = new PDMetadata(doc, new ByteArrayInputStream(xmpStream.toByteArray()));
            doc.getDocumentCatalog().setMetadata(pdMeta);

            // 保存临时文件
            File output = createTempFile(tempDir);
            doc.save(output);
            return output;
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToXml(Object obj) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    private static File createTempFile(String tempDir) {
        String filename = tempDir + File.separator + UUID.randomUUID() + ".pdf";
        return new File(filename);
    }

    // 配置参数类
    @Data
    public static class SignConfig {
        private String apiUrl;
        private String appId;
        private String appSecret;
        private String tenantId;
        private String keySn;
        private String tempDir = System.getProperty("java.io.tmpdir");

    }

    // 自定义异常
    public static class PdfProcessingException extends Exception {
        public PdfProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class SignatureException extends Exception {
        public SignatureException(String message) {
            super(message);
        }

        public SignatureException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static void main(String[] args) throws PdfProcessingException {
        ContractVO contractVO = new ContractVO();

// 1. 填充GeneralInformation
        GeneralInformation generalInformation = new GeneralInformation();
        generalInformation.setPlatform("E-Contract Platform v2.0");
        generalInformation.setContractGuid("CON-2023-001-ABCD");
        generalInformation.setContractType("Procurement");
        generalInformation.setBuyPlanGuid("BP-2023-001");
        generalInformation.setContractNO("CT-2023-001");
        generalInformation.setContractName("Annual IT Equipment Procurement");
        generalInformation.setEffectiveDate(new Date());
        generalInformation.setGrossAmount(new BigDecimal(1250000.00));
        generalInformation.setPaymentMethod("Bank Transfer");
        generalInformation.setPerformStartDate(new Date());
        generalInformation.setPerformEndDate(new Date());
        generalInformation.setPerformAddress("123 Main St, Government Complex, Washington DC");
        generalInformation.setPerformMethod("On-site Delivery and Installation");
        generalInformation.setQualityRequirement("All products must meet ISO 9001 standards");
        generalInformation.setBreachResponsibility("10% penalty for late delivery");
        generalInformation.setDisputeResolutionMethod("Arbitration in Washington DC");
        generalInformation.setSigningWebsite("www.econtract.gov");
        generalInformation.setSigningTime(new Date());
        generalInformation.setContractPayCount(4);
        generalInformation.setMultiAccountPayType("Proportional");
        generalInformation.setReserveStatus(true);
        generalInformation.setSupplierReserve("TechSolutions Inc");
        generalInformation.setReserveType("Performance Bond");
        generalInformation.setReserveMoney(1250000.00);
        generalInformation.setOtherInfo("Special requirements for data security");
        generalInformation.setIsPerformanceMoney(true);
        generalInformation.setPerformanceMoney(62500.00);
        generalInformation.setPerformanceMoneyType("");
        generalInformation.setIsRetentionMoney(true);
        generalInformation.setRetentionMoney(62500.00);
        generalInformation.setRetentionMoneyType("Percentage");
        generalInformation.setCostCompensationMoney(5000.00);
        generalInformation.setRAndDIncentivesMoney(10000.00);
        generalInformation.setCostCoverageType("Partial");
        generalInformation.setDeliveryDate(new Date());
        generalInformation.setProjectGuid("PRJ-2023-001");
        generalInformation.setProjectCode("IT-EQ-2023");
        generalInformation.setProjectName("IT Equipment Upgrade 2023");
        generalInformation.setBidGuid("BID-2023-001");
        generalInformation.setBidOpenTime(new Date());
        generalInformation.setPurMethod("Open Tender");
        generalInformation.setBidResultDate(new Date());
        generalInformation.setBidResultMoney(1250000.00);
        generalInformation.setSecret(false);
        generalInformation.setClauseSecret(false);
        generalInformation.setAllowMortgage(true);
        generalInformation.setIsAdvanceCharge(true);
        generalInformation.setAdvanceChargeMoney(250000.00);
        generalInformation.setIsOnline(true);
        generalInformation.setIsReserveDiscounts(false);
        generalInformation.setIsProductRequirement(true);
        generalInformation.setSettlementMode("Monthly");
        generalInformation.setInstallmentRequirement("25% upon signing, 25% upon delivery, 25% after installation, 25% after acceptance");
        generalInformation.setRiskSharing("Supplier bears 60% of risk during implementation");
        generalInformation.setPropertyOwnership("Transfers upon final payment");
        generalInformation.setBenefitDistribution("Government retains all intellectual property");
        generalInformation.setAfterSaleService("3 years on-site warranty");
        generalInformation.setControlMeasures("Weekly progress reports required");
        generalInformation.setReviewCriteria("Performance metrics defined in Annex A");
        contractVO.setGeneralInformation(generalInformation);

// 2. 填充PartyInformations
        ContractVO.PartyInformations partyInformations = new ContractVO.PartyInformations();
        List<PartyInformation> partyInformationList = new ArrayList<>();

// 第一个当事人信息
        PartyInformation buyerParty = new PartyInformation();
        buyerParty.setPartyType("Buyer");
        buyerParty.setPartyGuid("GOV-DC-001");
        buyerParty.setPartyCharacter("Government Agency");

        OrganizationInfo buyerOrgInfo = new OrganizationInfo();
        buyerOrgInfo.setOrgName("Department of Information Technology");
        buyerOrgInfo.setLegalRepresent("John Smith");
        buyerOrgInfo.setPrincipal("Michael Johnson");
        buyerOrgInfo.setOrgCode("DIT-001");
        buyerOrgInfo.setWebSite("www.dit.gov.dc");
        buyerOrgInfo.setSupplierSize(null);
        buyerOrgInfo.setSupplierFeatures(null);
        buyerOrgInfo.setForeignInvestmentType(null);
        buyerOrgInfo.setCountryType("Domestic");
        buyerOrgInfo.setUndertakingSubject("Primary");
        buyerOrgInfo.setPayee(false);
        buyerOrgInfo.setIsBidder(false);
        buyerOrgInfo.setTotalMoney(new BigDecimal(0.00));
        buyerOrgInfo.setIsEntrust(false);
        buyerParty.setOrganizationInfo(buyerOrgInfo);

        ContactInformation buyerContact = new ContactInformation();
        buyerContact.setContactName("Robert Brown");
        buyerContact.setTelephoneNumber("+1-202-555-0101");
        buyerContact.setEmail("robert.brown@dit.gov.dc");
        buyerContact.setInstantMessaging("Skype");
        buyerContact.setMsgAccount("robert.brown.dit");
        buyerContact.setMailingAddress("123 Government Ave, Washington DC");
        buyerContact.setPostalCode("20001");
        buyerParty.setContactInfo(buyerContact);

        BankAccountInformation buyerAccount = new BankAccountInformation();
        buyerAccount.setBankAccountName("Department of IT Treasury Account");
        buyerAccount.setBankName("Federal Reserve Bank");
        buyerAccount.setBankAddress("20th St and Constitution Ave NW, Washington DC");
        buyerAccount.setBankAccountNumber("XXXX-XXXX-XXXX-1234");
        buyerAccount.setBankNO("FRB-001");
        buyerParty.setAccountInfo(buyerAccount);
        partyInformationList.add(buyerParty);
        partyInformations.setPartyInformation(partyInformationList);
        contractVO.setPartyInformations(partyInformations);

// 3. 填充ContractObjectInformations
        ContractVO.ContractObjectInformations contractObjectInformations = new ContractVO.ContractObjectInformations();
        List<ContractObjectInformation> contractObjectList = new ArrayList<>();
// 标的信息
        ContractObjectInformation switchItem = new ContractObjectInformation();
        switchItem.setContractObjectNumber("ITEM-003");
        switchItem.setContractObjectName("Network Switch");
        switchItem.setContractObjectDesc("Cisco Catalyst 9300 Series Switch");
        switchItem.setContractObjectUnitPrice(10000.00);
        switchItem.setContractObjectQuantity(40.0);
        switchItem.setUnitOfMeasurement("Unit");
        switchItem.setContractObjectAmount(400000.00);
        switchItem.setDOrder(3);
        switchItem.setBuyPlanBillGuid("BP-ITEM-003");
        switchItem.setRequirementGuid("REQ-003");
        switchItem.setBrand("          ");
        switchItem.setIsImports(false);
        switchItem.setPurCatalogCode("IT-NETWORK-001");
        switchItem.setPlanTotalPrice(400000.00);
        switchItem.setPlanPurchaseNum(40.0);
        switchItem.setPlanPrice(10000.00);
        switchItem.setSpec("48-port 10G, 4x 40G uplinks, Stackable");
        switchItem.setGovService(false);

        BillStatisticalInformation switchStats = new BillStatisticalInformation();

        switchItem.setBillStatisticalInformation(switchStats);

        contractObjectList.add(switchItem);
        contractObjectInformations.setContractObjectInformation(contractObjectList);
        contractVO.setContractObjectInformations(contractObjectInformations);

// 4. 填充PaymentInformations
        ContractVO.PaymentInformations paymentInformations = new ContractVO.PaymentInformations();
        List<PaymentInformation> paymentList = new ArrayList<>();

        PaymentInformation payment1 = new PaymentInformation();
        payment1.setPeriods(1);
        payment1.setPayee("TechSolutions Inc");
        payment1.setPaymentAmount(new BigDecimal(312500.00));
        payment1.setPaymentDate(new Date());
        payment1.setPaymentTerms("Advance payment upon contract signing");

        PaymentInformation payment2 = new PaymentInformation();
        payment2.setPeriods(2);
        payment2.setPayee("TechSolutions Inc");
        payment2.setPaymentAmount(new BigDecimal(312500.00));
        payment2.setPaymentDate(new Date());
        payment2.setPaymentTerms("Upon delivery of 50% equipment");
        paymentList.add(payment1);
        paymentList.add(payment2);
        paymentInformations.setPaymentInformation(paymentList);
        contractVO.setPaymentInformations(paymentInformations);

// 5. 填充ItemInformations
        ContractVO.ItemInformations itemInformations = new ContractVO.ItemInformations();
        List<ItemInformation> itemList = new ArrayList<>();

        ItemInformation item1 = new ItemInformation();
        item1.setItemGuid("CLAUSE-001");
        item1.setItemName("Confidentiality");
        item1.setItemDescription("All parties agree to maintain confidentiality of contract terms and any proprietary information exchanged during performance.");

        ItemInformation item2 = new ItemInformation();
        item2.setItemGuid("CLAUSE-002");
        item2.setItemName("Intellectual Property");
        item2.setItemDescription("All intellectual property developed under this contract shall be owned by the Government.");
        itemList.add(item1);
        itemList.add(item2);
        itemInformations.setItemInformation(itemList);
        contractVO.setItemInformations(itemInformations);

// 6. 填充StatusInformation
        StatusInformation statusInformation = new StatusInformation();
        statusInformation.setStatusType("Active");

        ChangeInformation changeInfo = new ChangeInformation();
        changeInfo.setChangeReason("Scope adjustment");
        changeInfo.setChangeTime(new Date());
        changeInfo.setOriginalContent("20 servers");
        changeInfo.setContent("25 servers");
        changeInfo.setChangeProposer("Department of IT");

        statusInformation.setChangeInformation(changeInfo);
        contractVO.setStatusInformation(statusInformation);

// 7. 填充SecurityInformation
        SecurityInformation securityInformation = new SecurityInformation();
        securityInformation.setAlgorithm("AES-256");
        securityInformation.setEncryptedData("U2FsdGVkX1+E6x7VJ5J5XjTzF7Z3vJY=");
        securityInformation.setDigitalCertificate("MIIE5jCCAs6gAwIBAgIBATANBgkqhkiG9w0BAQsFADBOMQswCQYDVQQGEwJVUzETMBEGA1UECAwKV2FzaGluZ3RvbjEQMA4GA1UEBwwHQ29sdW1iaWExFTATBgNVBAoMDEdvdmVybm1lbnQwHhcNMjMwMTEwMDAwMDAwWhcNMjQwMTEwMjM1OTU5WjBOMQswCQYDVQQGEwJVUzETMBEGA1UECAwKV2FzaGluZ3RvbjEQMA4GA1UEBwwHQ29sdW1iaWExFTATBgNVBAoMDEdvdmVybm1lbnQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC8V7hZ6v5z8Q7X9J5XjTzF7Z3vJY=");
        securityInformation.setElectronicSignature("U2FsdGVkX1+E6x7VJ5J5XjTzF7Z3vJY=");
        securityInformation.setTimestamp("2023-01-10T14:30:22Z");
        contractVO.setSecurityInformation(securityInformation);

// 8. 填充SubSupplierInformations
        ContractVO.SubSupplierInformations subSupplierInformations = new ContractVO.SubSupplierInformations();
        List<SubSupplierInformation> subSupplierList = new ArrayList<>();

        SubSupplierInformation subSupplier1 = new SubSupplierInformation();
        subSupplier1.setSupplierGuid("SUB-001");
        subSupplier1.setSupplierCode("SUB-TECH-001");
        subSupplier1.setSupplierName("Network Installations LLC");
        subSupplier1.setSupplierSize("Medium");
        subSupplier1.setSupplierMoney(150000.00);
        subSupplier1.setMainContent("On-site installation and configuration of network equipment");

        SubSupplierInformation subSupplier2 = new SubSupplierInformation();
        subSupplier2.setSupplierGuid("SUB-002");
        subSupplier2.setSupplierCode("SUB-TECH-002");
        subSupplier2.setSupplierName("Data Center Services Inc");
        subSupplier2.setSupplierSize("Small");
        subSupplier2.setSupplierMoney(75000.00);
        subSupplier2.setMainContent("Server rack installation and cabling");

        subSupplierList.add(subSupplier1);
        subSupplierList.add(subSupplier2);
        subSupplierInformations.setSubSupplierInformation(subSupplierList);
        contractVO.setSubSupplierInformations(subSupplierInformations);

// 9. 填充AcceptanceInformations
        ContractVO.AcceptanceInformations acceptanceInformations = new ContractVO.AcceptanceInformations();
        List<AcceptanceInformation> acceptanceList = new ArrayList<>();

        AcceptanceInformation acceptanceInfo = new AcceptanceInformation();
        acceptanceInfo.setAcceptanceType("Technical");
        acceptanceInfo.setPerformMainBody("Department of IT Technical Team");
        acceptanceInfo.setIsInviteSupplier(true);
        acceptanceInfo.setIsInviteExpert(true);
        acceptanceInfo.setIsInviteServiceObject(false);
        acceptanceInfo.setIsProfessionalDetection(true);
        acceptanceInfo.setIsSpotCheck(true);
        acceptanceInfo.setSpotCheckProportion(20.0);
        acceptanceInfo.setIsDestructiveCheck(false);
        acceptanceInfo.setDestructiveCheckMethod("N/A");
        acceptanceInfo.setOtherPreparations("Test scripts and acceptance criteria must be prepared 2 weeks prior");
        acceptanceInfo.setInfewdays(14);
        acceptanceInfo.setAcceptanceDate(new Date());
        acceptanceInfo.setContractAcpMethod("Technical Testing");
        acceptanceInfo.setAcceptanceWorkArrange("3 days of testing with supplier participation");
        acceptanceInfo.setAcceptanceContent("Functional testing, performance testing, security testing");
        acceptanceInfo.setIsSampleReference(true);
        acceptanceInfo.setAcceptanceCriteria("All equipment must meet or exceed specifications with zero critical defects");
        acceptanceInfo.setAcceptanceProcedure("1. Visual inspection 2. Functional testing 3. Performance testing 4. Documentation review");
        acceptanceInfo.setOtherContent("Supplier must provide complete documentation including manuals and warranty information");

        acceptanceList.add(acceptanceInfo);
        acceptanceInformations.setAcceptanceInformation(acceptanceList);
        contractVO.setAcceptanceInformations(acceptanceInformations);

// 10. 填充NegotiationAcceptancePlanInformations
        ContractVO.NegotiationAcceptancePlanInformations negotiationAcceptancePlanInformations = new ContractVO.NegotiationAcceptancePlanInformations();
        List<NegotiationAcceptancePlanInformation> negotiationList = new ArrayList<>();

        NegotiationAcceptancePlanInformation negotiationInfo = new NegotiationAcceptancePlanInformation();
        negotiationInfo.setAcceptanceDate(new Date());
        negotiationInfo.setResultDescription("All equipment met specifications with minor documentation issues to be corrected");
        negotiationInfo.setAcceptanceMethod("Technical Testing");
        negotiationInfo.setAcceptanceCriteria("All equipment must meet or exceed specifications with zero critical defects");
        negotiationInfo.setCostCompensationMoney(5000.00);
        negotiationInfo.setRAndDIncentivesMoney(10000.00);
        negotiationInfo.setCostCoverageType("Partial");

        negotiationList.add(negotiationInfo);
        negotiationAcceptancePlanInformations.setNegotiationAcceptancePlanInformation(negotiationList);
        contractVO.setNegotiationAcceptancePlanInformations(negotiationAcceptancePlanInformations);

// 11. 填充ProjectManagerInformations
        ContractVO.ProjectManagerInformations projectManagerInformations = new ContractVO.ProjectManagerInformations();
        List<ProjectManagerInformation> projectManagerList = new ArrayList<>();

        ProjectManagerInformation projectManager = new ProjectManagerInformation();
        projectManager.setProjectManagerCode("PM-001");
        projectManager.setProjectManagerName("Robert Johnson");
        projectManager.setPhone("+1-202-555-0102");
        projectManager.setIdCard("ID-123456789");
        projectManager.setBirthday(new Date());
        projectManager.setSex("Male");
        projectManager.setTitle("Senior Project Manager");
        projectManager.setAddress("123 Government Ave, Washington DC");

        projectManagerList.add(projectManager);
        projectManagerInformations.setProjectManagerInformation(projectManagerList);
        contractVO.setProjectManagerInformations(projectManagerInformations);

// 12. 填充AttachmentInformations
        ContractVO.AttachmentInformations attachmentInformations = new ContractVO.AttachmentInformations();
        List<AttachmentInformation> attachmentList = new ArrayList<>();

        AttachmentInformation attachment1 = new AttachmentInformation();
        attachment1.setFileName("Technical_Specifications.pdf");
        attachment1.setAttachmentType("Technical");
        attachment1.setFilePath("/attachments/tech_specs.pdf");

        AttachmentInformation attachment2 = new AttachmentInformation();
        attachment2.setFileName("Signed_Contract.pdf");
        attachment2.setAttachmentType("Contract");
        attachment2.setFilePath("/attachments/signed_contract.pdf");

        AttachmentInformation attachment3 = new AttachmentInformation();
        attachment3.setFileName("Bid_Documents.zip");
        attachment3.setAttachmentType("Bid");
        attachment3.setAttachment("U2FsdGVkX1+E6x7VJ5J5XjTzF7Z3vJY=".getBytes());

        attachmentList.add(attachment1);
        attachmentList.add(attachment2);
        attachmentList.add(attachment3);
        attachmentInformations.setAttachmentInformation(attachmentList);
        contractVO.setAttachmentInformations(attachmentInformations);
        File file = generateSignedPdf1(contractVO, new File("D:\\YaoAn\\YaoAn\\pdf\\5aab5d27e24b4a368306753623fb0c41.pdf"));
        System.out.println(file);
    }
    public static File generateSignedPdf1(ContractVO vo,File pdfFile
    ) throws PdfProcessingException {
        try {
            // 1. 生成合同XML结构
            ContractVO xmlData = vo;
            ContractVOCleanerUtil.filterNullAndEmptyFields(xmlData);
            // 2. XML签名处理流程
            SignConfig config = new SignConfig();
            config.setApiUrl("http://114.115.214.104:803/");
            config.setAppId("0dfda7d1d11845a6a53c32374c8b7b2c");
            config.setAppSecret("ae5d60793a5845c19440a289e06d6008");
            config.setTenantId("1479363604116406272");
            config.setKeySn("d4fa9eff60ff4cfdbc7f06777b486568@SN");
            String signedXml = signXmlContent(xmlData, config);

            // 3. 将签名XML嵌入PDF
            return embedXmlToPdf(pdfFile, signedXml, config.getTempDir());
        } catch (JAXBException e) {
            throw new PdfProcessingException("XML处理失败", e);
        } catch (IOException e) {
            throw new PdfProcessingException("PDF文件操作失败", e);
        } catch (SignatureException e) {
            throw new PdfProcessingException("数字签名失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}