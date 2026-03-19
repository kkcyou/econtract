package com.yaoan.module.econtract.api.bpm.contract;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import com.reach.platform.sdk.base.client.OpenClientApi;
import com.reach.platform.sdk.base.config.SdkConfigure;
import com.reach.platform.sdk.base.entity.base.R;
import com.reach.platform.sdk.base.entity.cert.ApiSignReq;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractBillStatisticalVo;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractBillVo;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.PaymentPlanDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractEndXmlVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractToPdfVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.DigitalSignatureXML;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentPlanXML;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractGoodsConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractPaymentPlayConverter;
import com.yaoan.module.econtract.convert.gpx.GPXConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.version.FileVersionDO;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.ContractGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.*;
import com.yaoan.module.econtract.dal.mysql.order.AssociatedPlanMapper;
import com.yaoan.module.econtract.dal.mysql.order.GPMallGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.order.GoodsPurCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.version.FileVersionMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.econtract.service.cx.ChangXieService;
import com.yaoan.module.econtract.util.*;
import com.yaoan.module.econtract.util.xml.ContractXmlPdfUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yh.scofd.agent.HTTPAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.XmpSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ctc.wstx.shaded.msv_core.grammar.relaxng.datatype.CompatibilityDatatypeLibrary.namespaceURI;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;
import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;
import static com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums.BATCH;
import static com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums.UNION;
import static com.yaoan.module.econtract.service.contract.impl.ContractServiceImpl.createMultipartFile;
import static com.yaoan.module.econtract.service.contract.impl.ContractServiceImpl.downloadFileToTemp;

/**
 * ContractApi 实现类
 *
 * @author doujl
 */
@Slf4j
@Service
public class BpmContractApiImpl implements BpmContractApi {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private FileVersionEventPublisher fileVersionEventPublisher;
    @Resource
    private FileApi fileApi;
    @Resource
    private FileVersionMapper fileVersionMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private ChangXieService changXieService;
    @Resource
    private ContractApi contractApi;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private GPXProjectMapper gpxProjectMapper;
    @Resource
    private SupplierInfoMapper supplierInfoMapper;
    @Resource
    private GPXContractQuotationRelMapper gpxContractQuotationRelMapper;
    @Resource
    private PlanDetailInfoMapper planDetailMapper;
    @Resource
    private BidConfirmQuotationDetailMapper bidConfirmQuotationDetailMapper;
    @Resource
    private PackageDetailInfoMapper packageDetailInfoMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;
    @Resource
    private GoodsPurCatalogMapper goodsPurCatalogMapper;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private ContractGoodsMapper contractGoodsMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractParamFieldMapper contractParamFieldMapper;
    @Resource
    private ContractFileMapper contractFileMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;



    @Override
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public void notifyBpmContactApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        BpmContract bpmContract = bpmContractMapper.selectById(businessKey);
        if (bpmContract != null) {
            //1.更新申请表状态
            bpmContract.setResult(statusEnums.getResult());
            bpmContractMapper.updateById(bpmContract);
            //2.更新合同表状态
            ContractDO contractDO = contractMapper.selectById(bpmContract.getContractId());
            if (contractDO != null) {
                //内部审批通过之后，合同变成 “待发送状态”
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.TO_BE_SENT.getCode());
//                    if(ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(contractDO.getUpload()) ||ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.getUpload())) {
//                        if (contractDO.getPlatform().equals(PlatformEnums.GPMS_GPX.getCode())) {
//                            //交易的合同由于下一岗就是签署，所以此处特殊处理
//                            Integer status = ContractStatusEnums.TO_BE_SIGNED.getCode();
//                            if (contractApi.isNeedSignet(null,bpmContract.getContractId())) {
//                                //如果配置用印申请参数，则需要走用印申请
//                                status = ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode();
//                            }
//                            contractDO.setStatus(status);
//                        }
//                    }
                    //签署方签署时合同与签署方关系表中添加签署的标识
                    signatoryRelMapper.update(null, new LambdaUpdateWrapper<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractDO.getId()).set(SignatoryRelDO::getIsSign, true));

                    //转pdf
                    try {
//                        if (contractDO.getPdfFileId() == 0) {
                            //使用黑龙江模式，使用合同富文本转pdf
//                            List<FileVersionDO> fileVersionDOList = fileVersionMapper.selectList(new LambdaQueryWrapperX<FileVersionDO>().eq(FileVersionDO::getBusinessId,contractDO.getId()).orderByAsc(FileVersionDO::getId));
                            Long fileAddId = contractDO.getFileAddId();
//                            if(fileVersionDOList.size() > 0 ){
//                                fileAddId = fileVersionDOList.get(0).getCopyFileId();
//                            }else{
//                                fileAddId = contractDO.getFileAddId();
//                            }
//                        Long pdfFileId = toPdf(new ContractToPdfVO().setFileAddId(fileAddId).setName(contractDO.getName()));
                        //如果是上传合同类型，则已经有pdf了，且该类型的合同没有doc和富文本
                        if (!ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode().equals(contractDO.getUpload())) {
                            Long pdfFileId = toPdf(new ContractToPdfVO().setFileAddId(fileAddId).setName(contractDO.getName()).setContent(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8)).setName(contractDO.getName()));
                            //生成xml 并添加到pdf中（暂时不上生产了）
                            try {
                                if (systemConfigApi.ifAddXml(SystemConfigKeyEnums.IF_ADD_XML.getKey())) {
                                    contractDO.setPdfFileId(toXmlPdf(contractDO, pdfFileId));
                                } else {
                                    contractDO.setPdfFileId(pdfFileId);
                                }
                            } catch (Exception e) {
                                contractDO.setPdfFileId(pdfFileId);
                            }
                        }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("电子合同转PDF失败!");
//                        try {
//                            Long pdfFileId = convertRtf2PdfByJava(new ContractToPdfVO().setContent(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8)).setName(contractDO.getName()));
//                            contractDO.setPdfFileId(pdfFileId);
//                        }catch (Exception e1){
//                            e1.printStackTrace();
//                            log.error("电子合同Java转PDF失败!");
//                            throw  new RuntimeException(e.getMessage());
//                        }
                        throw exception(SYSTEM_ERROR, "电子合同转PDF失败!失败原因：" + e.getMessage());

                    }
                    contractMapper.updateById(contractDO.setApproveTime(LocalDateTime.now()));
                    //发送作为单独的按钮操作
                    //交易比较特殊，由于没有确认环节，此处直接发送
                    if (PlatformEnums.GPMS_GPX.getCode().equals(contractDO.getPlatform())) {
                        contractApi.productApproveSendEcms("", contractDO.getId());
                    }
                    return;
                }
                // 内部审批被退回之后，合同变成 “审批被退回”
                if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.APPROVE_BACK.getCode());
                }
                //结束节点不通过，变成不通过
                if (BpmProcessInstanceResultEnum.REJECT == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.CHECK_REJECTED.getCode());
                }
                //被退回再次请求，变成审批中
                if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
                }
                contractMapper.updateById(contractDO.setApproveTime(LocalDateTime.now()));
            }
        }
    }

    private Long toXmlPdf(ContractDO contractDO, Long pdfFileId) throws Exception {
        FileDTO fileDTO = fileApi.selectById(pdfFileId);
        File file = downloadFileToTemp(fileDTO);
        ContractOrderExtDO contractExtDO = contractOrderExtMapper.selectById(contractDO.getId());
        TradingContractExtDO orderContractDO = tradingContractExtMapper.selectById(contractDO.getId());
        SupplyDTO supply = null;
        // 标的信息列表
        List<ContractBillVo> contractBillVOS = new ArrayList<>();
        // 支付信息列表
        List<PaymentPlanDTO> paymentPlanDTOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(contractExtDO)) {
            supply = supplyApi.getSupply(contractExtDO.getSupplierId());
            if (PlatformEnums.GPMS_GPX == PlatformEnums.getInstance(contractDO.getPlatform())) {
                contractBillVOS = getContractObjectInformations(contractExtDO);
            } else {
                contractBillVOS = contractBillsProcessor(contractExtDO);
            }
            // 支付信息列表
            paymentPlanDTOS = contractPaymentPlanProcessor(contractExtDO);
        }
        List<TradingSupplierDO> tradingSupplierDOS = tradingSupplierMapper.selectList(TradingSupplierDO::getContractId, contractDO.getId());

        // 履约验收要求信息列表
        List<OrderContractParamFieldDO> orderContractParamFieldDOS = contractParamFieldMapper.selectList(
                new LambdaQueryWrapperX<OrderContractParamFieldDO>().eqIfPresent(OrderContractParamFieldDO::getContractId, contractDO.getId()));
        //附件列表
        List<ContractFileDO> contractFileDOS = contractFileMapper.selectList(new LambdaQueryWrapperX<ContractFileDO>()
                .eq(ContractFileDO::getContractId, contractDO.getId()));
        // 公司信息
        List<CompanyRespDTO> companyByIds = companyApi.getCompanyByIds(Collections.singletonList(Long.parseLong(contractDO.getCreator())));
        // 相对方信息
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId());
        List<Relative> relatives = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
            List<String> signatoryIds = signatoryRelDOS.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            relatives = relativeMapper.selectList(Relative::getId, signatoryIds);
        }
        File pdf = ContractXmlPdfUtil.generateSignedPdf(contractDO, contractExtDO, orderContractDO, supply, contractBillVOS, paymentPlanDTOS, orderContractParamFieldDOS, file, contractFileDOS, companyByIds, relatives, tradingSupplierDOS);
        Long xmlPdf = fileApi.uploadFile(contractDO.getName() + "XML" + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING_XML.getPath() + IdUtil.fastSimpleUUID() + ".pdf", createMultipartFile(pdf).getBytes());
        return xmlPdf;
    }

    public List<PaymentPlanDTO> contractPaymentPlanProcessor(ContractOrderExtDO orderContractDO) {
        //获取合同支付计划
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().eqIfPresent(PaymentScheduleDO::getContractId, orderContractDO.getId()));
        List<PaymentPlanDTO> planDTOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
            planDTOS = ContractPaymentPlayConverter.INSTANCE.toVoList(paymentScheduleDOList);
            for (int i = 0; i < planDTOS.size(); i++) {
                PaymentPlanDTO paymentPlanDTO = planDTOS.get(i);
                if (ObjectUtil.isEmpty(paymentPlanDTO.getMoney())) {
                    paymentPlanDTO.setMoney(orderContractDO.getTotalMoney().doubleValue());
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPayee())) {
                    paymentPlanDTO.setPayee(orderContractDO.getSupplierName());
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPeriods())) {
                    paymentPlanDTO.setPeriods(i);
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPayDate())) {
                    paymentPlanDTO.setPayDate(System.currentTimeMillis());
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPayTerm())) {
                    paymentPlanDTO.setPayTerm("");
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPayProportion())) {
                    paymentPlanDTO.setPayProportion(0.0);
                }
            }
        }
        return planDTOS;
    }

    public List<ContractBillVo> contractBillsProcessor(ContractOrderExtDO orderContractDO) {
        List<ContractGoodsDO> goods = contractGoodsMapper.selectList(ContractGoodsDO::getContractId, orderContractDO.getId());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goods)) {
            List<ContractBillVo> contractBillVos = ContractGoodsConverter.INSTANCE.toVoList(goods);
            if (CollectionUtil.isNotEmpty(contractBillVos)) {
                contractBillVos.forEach(item -> {
                    ContractBillStatisticalVo statisticalVo = new ContractBillStatisticalVo();
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceName())) {
                        statisticalVo.setSupplierName(orderContractDO.getPropertyServiceName());
                    }
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceType())) {
                        statisticalVo.setSupplierSize(orderContractDO.getPropertyServiceType());
                    }
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceAddress())) {
                        statisticalVo.setZoneCode(orderContractDO.getPropertyServiceAddress());
                    }
                    item.setContractBillStatisticalVo(statisticalVo);
                });
            }
            return contractBillVos;
        } else {
            if (!PlatformEnums.GPMS_GPX.getCode().equals(orderContractDO.getPlatform())) {
                //京东卖场，协议定点，框彩，服务超市
                String orderId = orderContractDO.getOrderId();
                List<GoodsDO> goodsDOS = new ArrayList<>();
                //合并订单信息
                List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getOrderId, orderId);
                if (CollectionUtil.isNotEmpty(orderRelDOS) && orderRelDOS.size() > 1) {
                    goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>()
                            .in(GoodsDO::getOrderId, orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList())));
                } else {
                    goodsDOS = gpMallGoodsMapper.selectList(GoodsDO::getOrderId, orderId);
                }
                Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
                List<ContractBillVo> vos2 = ContractGoodsConverter.INSTANCE.toVOS2(goodsDOS);
                AssociatedPlanDO associatedPlanDO = associatedPlanMapper.selectOne(AssociatedPlanDO::getOrderId, orderContractDO.getOrderId());
                List<GoodsPurCatalogDO> goodsPurCatalogDOS = goodsIds.size() == 0 ? null : goodsPurCatalogMapper.selectList(GoodsPurCatalogDO::getGoodsId, goodsIds);
                Map<String, GoodsPurCatalogDO> goodsPurCatalogDOMap = CollectionUtils.convertMap(goodsPurCatalogDOS, GoodsPurCatalogDO::getGoodsId);
                Double totalMoney = 0.0;
                vos2.forEach(vo -> {
                    vo.setIsImports(associatedPlanDO == null ? null : associatedPlanDO.getIsImports());
                    if (ObjectUtil.isNotEmpty(goodsPurCatalogDOMap)) {
                        vo.setPurCatalogCode(goodsPurCatalogDOMap.get(vo.getId()) == null ? null : goodsPurCatalogDOMap.get(vo.getId()).getPurCatalogCode());
                    }
                    vo.setContractBillGuid(IdUtil.fastSimpleUUID());
                    if (StringUtils.isEmpty(vo.getSpec())) {
                        vo.setSpec("无");
                    }
                    ContractBillStatisticalVo statisticalVo = new ContractBillStatisticalVo();
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceName())) {
                        statisticalVo.setSupplierName(orderContractDO.getPropertyServiceName());
                    }
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceType())) {
                        statisticalVo.setSupplierSize(orderContractDO.getPropertyServiceType());
                    }
                    if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceAddress())) {
                        statisticalVo.setZoneCode(orderContractDO.getPropertyServiceAddress());
                    }
                    vo.setContractBillStatisticalVo(statisticalVo);
                });
                for (ContractBillVo contractBillVo : vos2) {
                    totalMoney = totalMoney + contractBillVo.getTotalPrice();
                }
                return vos2;
            }
        }
        return Collections.emptyList();
    }

    private List<ContractBillVo> getContractObjectInformations(ContractOrderExtDO orderContractDO) {
        //电子交易实体类
        GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, orderContractDO.getProjectGuid());
        List<GPXContractQuotationRelDO> contractQuotationRelDOList = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, orderContractDO.getId());
        Map<String, GPXContractQuotationRelDO> contractQuotationRelDOMap = new HashMap<String, GPXContractQuotationRelDO>();
        if(CollectionUtil.isNotEmpty(contractQuotationRelDOList)){
            contractQuotationRelDOMap= CollectionUtils.convertMap(contractQuotationRelDOList,GPXContractQuotationRelDO::getPackageDetailId);
        }
        //包详情list
        List<PackageDetailInfoDO> packageDetailInfoDOS = new ArrayList<>();
        if (UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
            packageDetailInfoDOS = packageDetailInfoMapper.getPackageDetailList4Union(orderContractDO.getId());
        } else {
            packageDetailInfoDOS = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, orderContractDO.getBidGuid());

        }
//            List<PackageDetailInfoDO> packageDetailInfoDOS = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, tradingContractDO.getBidGuid());


        List<ContractBillVo> contractBillVos2 = convertToContractBillVoListV1(packageDetailInfoDOS, orderContractDO.getId(),
                projectDO, contractQuotationRelDOMap);
        if(ObjectUtil.isNotEmpty(contractBillVos2)&&contractBillVos2.size()==1){
            contractBillVos2.get(0).setTotalPrice(orderContractDO.getTotalMoney().doubleValue());
        }
        if(CollectionUtil.isNotEmpty(contractBillVos2)){
            contractBillVos2.forEach(vo->{
                ContractBillStatisticalVo statisticalVo = new ContractBillStatisticalVo();
                if(StringUtils.isNotEmpty(orderContractDO.getPropertyServiceName())){
                    statisticalVo.setSupplierName(orderContractDO.getPropertyServiceName());
                }
                if(StringUtils.isNotEmpty(orderContractDO.getPropertyServiceType())){
                    statisticalVo.setSupplierSize(orderContractDO.getPropertyServiceType());
                }
                if(StringUtils.isNotEmpty(orderContractDO.getPropertyServiceAddress())){
                    statisticalVo.setZoneCode(orderContractDO.getPropertyServiceAddress());
                }
                vo.setContractBillStatisticalVo(statisticalVo);
            });
        }
        return contractBillVos2;
    }

    public List<ContractBillVo> convertToContractBillVoListV1(List<PackageDetailInfoDO> packageDetailInfoDOList, String contractId, GPXProjectDO projectDO, Map<String, GPXContractQuotationRelDO> contractQuotationRelDOMap) {
        if (UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
            return packageDetailInfoDOList.stream()
                    .map(detail -> {
                        ContractBillVo contractBillVo = new ContractBillVo();
                        //合同明细唯一识别码设置uuid
                        contractBillVo.setContractBillGuid(UUID.randomUUID().toString().replace("-", ""));
                        //计划明细表
                        GPXContractQuotationRelDO contractQuotationRelDO = contractQuotationRelDOMap.get(detail.getDetailId());
                        if (ObjectUtil.isNotNull(contractQuotationRelDO)) {
                            contractBillVo.setTotalPrice(Double.parseDouble(contractQuotationRelDO.getTotalPrice()));
                            contractBillVo.setPurchaseNum(contractQuotationRelDO.getCount().doubleValue());
                            contractBillVo.setPrice(contractQuotationRelDO.getUnitPrice().doubleValue());
                        }
                        contractBillVo.setBuyPlanBillGuid(contractQuotationRelDO.getOutsiteId());

                        //对应采购计划明细的唯一识别码
//                    contractBillVo.setBuyPlanBillGuid(detail.getPlanDetailId());
                        //商品/服务名称
                        contractBillVo.setGoodsName(detail.getDeatilName());
                        //计量单位
                        contractBillVo.setUnit(detail.getUnit());
                        //是否进口产品采购(1:是,0:否)
                        contractBillVo.setIsImports(detail.getIsImported());
                        //采购目录代码
                        contractBillVo.setPurCatalogCode(detail.getCatalogueCode());
                        //计划采购总价
                        contractBillVo.setPlanTotalPrice(detail.getPlanTotalPrice().doubleValue());
                        //计划采购数量
                        contractBillVo.setPlanPurchaseNum(detail.getPlanNumber().doubleValue());
                        //计划采购单价
                        contractBillVo.setPlanPrice(detail.getPlanPrice().doubleValue());
                        BidConfirmQuotationDetailDO bidConfirmQuotationDetailDO = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>().eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                        //规格参数/服务内容
                        if (ObjectUtil.isNotEmpty(bidConfirmQuotationDetailDO) && StringUtils.isNotEmpty(bidConfirmQuotationDetailDO.getModelSpecification())) {
                            contractBillVo.setSpec(bidConfirmQuotationDetailDO.getModelSpecification());
                        } else {
                            contractBillVo.setSpec("【无】");
                        }
                        //总价格
//                        contractBillVo.setTotalPrice(bidConfirmQuotationDetailDO.getTotalPrice() != null ? Double.parseDouble(bidConfirmQuotationDetailDO.getTotalPrice()) : null);
//                        //采购数量
//                        contractBillVo.setPurchaseNum(bidConfirmQuotationDetailDO.getCount() != null ? bidConfirmQuotationDetailDO.getCount().doubleValue() : null);
//                        contractBillVo.setPrice(bidConfirmQuotationDetailDO.getUnitPrice() != null ? bidConfirmQuotationDetailDO.getUnitPrice().doubleValue() : null);
                        return contractBillVo;
                    })
                    .collect(Collectors.toList());
        }
        if (BATCH.getCode().equals(projectDO.getBiddingMethodCode())) {
            return packageDetailInfoDOList.stream()
                    .map(detail -> {
                        ContractBillVo contractBillVo = new ContractBillVo();
                        //合同明细唯一识别码设置uuid
                        contractBillVo.setContractBillGuid(UUID.randomUUID().toString().replace("-", ""));
                        //计划明细表
                        GPXContractQuotationRelDO contractQuotationRelDO = contractQuotationRelDOMap.get(detail.getDetailId());
                        if (ObjectUtil.isNotNull(contractQuotationRelDO)) {
                            contractBillVo.setTotalPrice(Double.parseDouble(contractQuotationRelDO.getTotalPrice()));
                            contractBillVo.setPurchaseNum(contractQuotationRelDO.getCount().doubleValue());
                            contractBillVo.setPrice(contractQuotationRelDO.getUnitPrice().doubleValue());
                        }
                        contractBillVo.setBuyPlanBillGuid(contractQuotationRelDO.getOutsiteId());

                        //对应采购计划明细的唯一识别码
//                    contractBillVo.setBuyPlanBillGuid(detail.getPlanDetailId());
                        //商品/服务名称
                        contractBillVo.setGoodsName(detail.getDeatilName());
                        //计量单位
                        contractBillVo.setUnit(detail.getUnit());
                        //是否进口产品采购(1:是,0:否)
                        contractBillVo.setIsImports(detail.getIsImported());
                        //采购目录代码
                        contractBillVo.setPurCatalogCode(detail.getCatalogueCode());
                        //计划采购总价
                        contractBillVo.setPlanTotalPrice(detail.getPlanTotalPrice().doubleValue());
                        //计划采购数量
                        contractBillVo.setPlanPurchaseNum(detail.getPlanNumber().doubleValue());
                        //计划采购单价
                        contractBillVo.setPlanPrice(detail.getPlanPrice().doubleValue());
                        BidConfirmQuotationDetailDO bidConfirmQuotationDetailDO = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>().eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                        //规格参数/服务内容
                        if (ObjectUtil.isNotEmpty(bidConfirmQuotationDetailDO) && StringUtils.isNotEmpty(bidConfirmQuotationDetailDO.getModelSpecification())) {
                            contractBillVo.setSpec(bidConfirmQuotationDetailDO.getModelSpecification());
                        } else {
                            contractBillVo.setSpec("【无】");
                        }
                        //总价格
//                        contractBillVo.setTotalPrice(bidConfirmQuotationDetailDO.getTotalPrice() != null ? Double.parseDouble(bidConfirmQuotationDetailDO.getTotalPrice()) : null);
//                        //采购数量
//                        contractBillVo.setPurchaseNum(bidConfirmQuotationDetailDO.getCount() != null ? bidConfirmQuotationDetailDO.getCount().doubleValue() : null);
//                        contractBillVo.setPrice(bidConfirmQuotationDetailDO.getUnitPrice() != null ? bidConfirmQuotationDetailDO.getUnitPrice().doubleValue() : null);
                        return contractBillVo;
                    })
                    .collect(Collectors.toList());
        }
//一般项目采购-多中标供应商-需要供应商名字匹配
        Boolean isOneSup = true;
        ContractOrderExtDO contractDO = contractOrderExtMapper.selectOne(ContractOrderExtDO::getId, contractId);
        if (ObjectUtil.isNotNull(contractDO)) {
            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, contractDO.getBuyPlanPackageId());
            if (ObjectUtil.isNotNull(packageInfoDO)) {
                if (ObjectUtils.isNotEmpty(packageInfoDO.getSupplierType()) && 2 == packageInfoDO.getSupplierType()) {
                    isOneSup = false;
                }
            }

        }
        Boolean finalIsOneSup = isOneSup;

        return packageDetailInfoDOList.stream()
                .map(detail -> {
                    ContractBillVo contractBillVo = new ContractBillVo();
                    //合同明细唯一识别码设置uuid
                    contractBillVo.setContractBillGuid(UUID.randomUUID().toString().replace("-", ""));
                    //计划明细表

                    PlanDetailInfoDO planDetailInfoDO = planDetailMapper.selectById(detail.getPlanDetailId());
                    contractBillVo.setBuyPlanBillGuid(planDetailInfoDO.getOutsiteId());

                    //对应采购计划明细的唯一识别码
//                    contractBillVo.setBuyPlanBillGuid(detail.getPlanDetailId());
                    //商品/服务名称
                    contractBillVo.setGoodsName(detail.getDeatilName());
                    //计量单位
                    contractBillVo.setUnit(detail.getUnit());
                    //是否进口产品采购(1:是,0:否)
                    contractBillVo.setIsImports(detail.getIsImported());
                    //采购目录代码
                    contractBillVo.setPurCatalogCode(detail.getCatalogueCode());
                    //计划采购总价
                    contractBillVo.setPlanTotalPrice(detail.getPlanTotalPrice().doubleValue());
                    //计划采购数量
                    contractBillVo.setPlanPurchaseNum(detail.getPlanNumber().doubleValue());
                    //计划采购单价
                    contractBillVo.setPlanPrice(detail.getPlanPrice().doubleValue());
                    BidConfirmQuotationDetailDO bidConfirmQuotationDetailDO = new BidConfirmQuotationDetailDO();
                    if (finalIsOneSup) {
                        bidConfirmQuotationDetailDO = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>()
                                        .eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                    } else {
                        SupplierInfoDO supplierInfoDO = supplierInfoMapper.selectOne(new LambdaQueryWrapperX<SupplierInfoDO>().eq(SupplierInfoDO::getSupplierId, contractDO.getSupplierId())
                                .eq(SupplierInfoDO::getPackageId, contractDO.getBuyPlanPackageId()));
                        bidConfirmQuotationDetailDO = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>()
                                        .eq(BidConfirmQuotationDetailDO::getSupplierName, supplierInfoDO.getSupplierName())
                                        .eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                    }
                    if (ObjectUtil.isNotNull(bidConfirmQuotationDetailDO)) {

                        //规格参数/服务内容
                        if (ObjectUtil.isNotEmpty(bidConfirmQuotationDetailDO) && StringUtils.isNotEmpty(bidConfirmQuotationDetailDO.getModelSpecification())) {
                            contractBillVo.setSpec(bidConfirmQuotationDetailDO.getModelSpecification());
                        } else {
                            contractBillVo.setSpec("【无】");
                        }
                        //可能会有修改过的标的
                        GPXContractQuotationRelDO contractQuotationRelDO = contractQuotationRelDOMap.get(detail.getDetailId());
                        if (ObjectUtil.isNotNull(contractQuotationRelDO)) {
                            contractBillVo.setTotalPrice(Double.parseDouble(contractQuotationRelDO.getTotalPrice()));
                            contractBillVo.setPurchaseNum(contractQuotationRelDO.getCount().doubleValue());
                            contractBillVo.setPrice(contractQuotationRelDO.getUnitPrice().doubleValue());
                        } else {
                            //总价格
                            contractBillVo.setTotalPrice(bidConfirmQuotationDetailDO.getTotalPrice() != null ? Double.parseDouble(bidConfirmQuotationDetailDO.getTotalPrice()) : null);
                            //采购数量
                            contractBillVo.setPurchaseNum(bidConfirmQuotationDetailDO.getCount() != null ? bidConfirmQuotationDetailDO.getCount().doubleValue() : null);
                            contractBillVo.setPrice(bidConfirmQuotationDetailDO.getUnitPrice() != null ? bidConfirmQuotationDetailDO.getUnitPrice().doubleValue() : null);
                        }
                    }
                    return contractBillVo;
                })
                .collect(Collectors.toList());
    }

    private Long setXmlToPdf(ContractDO contractDO, Long pdfFileId) {
        try {
            FileDTO fileDTO = fileApi.selectById(pdfFileId);
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
                JAXBContext endContext =  JAXBContext.newInstance(ContractEndXmlVO.class);

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
                customSchema.setData("contractData",base64EncodedXml);

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
                File outputFile = new File(READY_TO_UPLOAD_PATH + IdUtil.fastSimpleUUID() + ".pdf");
//                File outputFile = new File(READY_TO_UPLOAD_PATH + IdUtil.fastSimpleUUID() + ".pdf");
                document.save(outputFile);
                document.close();
                Long xmlPdf = fileApi.uploadFile(contractDO.getName()+"XML" + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING_XML.getPath() + IdUtil.fastSimpleUUID() + ".pdf", createMultipartFile(outputFile).getBytes());
                //删除临时文件
                try {
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                } catch (Exception e) {
                    System.out.println("删除临时文件异常:"+outputFile.getPath());
                }
                return xmlPdf;
            }else {
                throw exception(SYSTEM_ERROR, "签名失败!失败原因：" +call);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(SYSTEM_ERROR, "生成xml失败!失败原因：" + e.getMessage());
        }
    }

    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;

    /**
     * 优先html转pdf
     * 没有富文本
     * 则word转pdf
     */
    public Long toPdf(ContractToPdfVO contractToPdfVO) throws Exception {
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        //替换名称中的特殊字符
        s = s.replaceAll("/", "");
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本转pdf 同步黑龙江模式
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {

//            if(1== 1){
//                convertRtf2Pdf(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
//            }else{
//                PdfConvertHtmlUtil.rtf2Pdf1(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
//            }
//            convertRtf2Pdf(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
            try {
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
            } catch (Exception e) {
                System.out.println("【WK html转换pdf】处理异常！" + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));

        } else {
            //docx生成pdf
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractToPdfVO.getFileAddId()));
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()));
//            Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//
//            AsposeUtil.docx2Pdf(localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()),
//                    localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf"); //红色水印Evaluation Only.
//          //  Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            if (StringUtils.isNotBlank(contractToPdfVO.getName())) {
//                pdfFileId = fileApi.uploadFile(contractToPdfVO.getName() + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            } else {
//                pdfFileId = fileApi.uploadFile(FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            }
            //将word清稿,然后转pdf
            Long cleanDraftFileId = changXieService.cleandraftV2(contractToPdfVO.getFileAddId());
            //畅写转pdf
            pdfFileId = changXieService.converterDocx2PdfV2(cleanDraftFileId, FileUploadPathEnum.CONTRACT_DRAFT);
            return pdfFileId;
        }


        FileUtil.del(localFolderPath);
        return pdfFileId;
    }

    //    public void convertRtf2Pdf(String content, String targetPath) throws Exception {
//        try {
//            System.out.println("【友虹服务对接】触发了一次友虹组件任务！");
//            File outputFile = new File(targetPath);
//            System.out.println("【友虹服务对接】targetPath:" + targetPath);
//            final HTTPAgent agent = new HTTPAgent(SpringUtil.getProperty("components.agent.test.url"));
//            FileOutputStream out = new FileOutputStream(outputFile);
//            agent.htmlStrToPDF(content, out);
//            agent.close();
//            System.out.println("【友虹服务对接】友虹组件任务处理完成！");
//        } catch (Exception e) {
//            System.out.println("【友虹服务对接】处理异常！" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    public void convertRtf2Pdf(String content, String targetPath) throws Exception {
        File outputFile = new File(targetPath);
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addSystemFonts();
        converterProperties.setFontProvider(fontProvider);
        List<String> keys = new ArrayList<>();
        keys.add(SystemConfigKeyEnums.IF_FILE_CONVERT_SELF.getKey());
        String flag = systemConfigApi.getConfigsByCKeys(keys).get(0).getCValue();
        log.debug("【系统文件配置】是否使用本身转换配置！flag:{}", flag);
        if ("true".equals(flag)) {
            // 调用rtf2Pdf方法进行转换
            PdfConvertHtmlUtil.rtf2Pdf1(content, targetPath);
        } else {
            log.debug("【友虹服务对接】触发了一次友虹组件任务！");
            // 如果输入文件不是RTF格式，则调用officeToPDF方法进行转换
            final HTTPAgent agent = new HTTPAgent(SpringUtil.getProperty("components.agent.test.url"));
            FileOutputStream out = new FileOutputStream(outputFile);
            agent.htmlStrToPDF(content, out);
            agent.close();
            log.debug("【友虹服务对接】友虹组件任务处理完成！");
        }
    }

    public Long convertRtf2PdfByJava(ContractToPdfVO contractToPdfVO) throws Exception {
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本转pdf 同步黑龙江模式
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {
            String targetPath = localFolderPath + "/" + s + ".pdf";
            File outputFile = new File(targetPath);
            ConverterProperties converterProperties = new ConverterProperties();
            FontProvider fontProvider = new DefaultFontProvider();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            log.debug("生产pdf！");
            PdfConvertHtmlUtil.rtf2Pdf1(contractToPdfVO.getContent(), targetPath);
            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));

        } else {


        }
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }
}
