package com.yaoan.module.econtract.service.contract.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractStatusDTO;
import com.yaoan.module.econtract.api.contract.dto.FileAcceptDTO;
import com.yaoan.module.econtract.api.contract.dto.SyncContractStatusDTO;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.*;
import com.yaoan.module.econtract.api.gcy.zcd.dto.ResponseDTO;
import com.yaoan.module.econtract.controller.admin.aop.service.OutOpenApiService;
import com.yaoan.module.econtract.convert.contract.OrderContractConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractParamFieldConverter;
import com.yaoan.module.econtract.convert.contract.trading.TradingSupplierConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractGoodsConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractPaymentPlayConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractSupplierConverter;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.ContractGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.*;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import com.yaoan.module.econtract.enums.BusinessTokenConfigEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.enums.neimeng.AttachmentTypeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.supervise.SupplierSizeEnum;
import com.yaoan.module.econtract.service.contract.ContractBakService;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.yaoan.module.system.api.user.SupplyApi;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR_V2;
import static com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums.UNION;
import static com.yaoan.module.econtract.service.contract.impl.ContractServiceImpl.createMultipartFile;


@Service
@Slf4j
public class ContractBakServiceImpl implements ContractBakService {
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private EcmsGcyBuyPlanService gcyBuyPlanService;
    @Resource
    private ContractParamFieldMapper contractParamFieldMapper;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private ContractFileMapper contractFileMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private GPMallOrderMapper gpMallOrderMapper;
    @Resource
    private ContractGoodsMapper contractGoodsMapper;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private GPMallProjectMapper gpMallProjectMapper;
    @Resource
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    @Resource
    private GoodsPurCatalogMapper goodsPurCatalogMapper;
    @Resource
    private GPXProjectMapper gpxProjectMapper;
    @Resource
    private SupplyApi supplyApi;

    @Resource
    private PlanDetailInfoMapper planDetailMapper;

    @Resource
    private PlanInfoMapper planInfoMapper;
    @Resource
    private SupplierInfoMapper supplierInfoMapper;
    @Resource
    private BidConfirmQuotationDetailMapper bidConfirmQuotationDetailMapper;
    @Resource
    private PackageDetailInfoMapper packageDetailInfoMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private GPXContractQuotationRelMapper gpxContractQuotationRelMapper;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private RegionApi regionApi;
    @Resource
    private OutOpenApiService outOpenApiService;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private GPMallOrderOldMapper gpMallOrderOldMapper;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    // 合同备案接口
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void contractBak(String id) {
        System.out.println("【合同备案】合同备案开始,id---->" + id);
        ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>()
                .eqIfPresent(ContractOrderExtDO::getId, id)
                .notIn(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(),
                        HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(),
                        HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode())
//                .select(ContractOrderExtDO::getPdfFileId,
//                        ContractOrderExtDO::getId, ContractOrderExtDO::getOrderId, ContractOrderExtDO::getPlatform)
        );
        if (ObjectUtil.isEmpty(orderContractDO)) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"合同备案信息异常");
        }
        if (!PlatformEnums.GPMS_GPX.getCode().equals(orderContractDO.getPlatform())) {
            List<ContractOrderRelDO> contractOrderRelDOList =  contractOrderRelMapper.selectList(new LambdaQueryWrapperX<ContractOrderRelDO>().eq(ContractOrderRelDO::getContractId, id));
            if(contractOrderRelDOList.size() == 0){
                List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderOldMapper.selectList(DraftOrderInfoDO::getOrderGuid, orderContractDO.getOrderId());
                if(CollectionUtil.isEmpty(draftOrderInfoDOS)){
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, id + "找不到对应订单");
                }else {
                    List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(AssociatedPlanDO::getOrderId, draftOrderInfoDOS.get(0).getOrderGuid());
                    if (ObjectUtil.isEmpty(associatedPlanDOS)) {
                        throw exception(ErrorCodeConstants.GOMall_Query_Error, id + "无计划合同不用推送监管备案");
                    }
                    if("0".equals(associatedPlanDOS.get(0).getBuyPlanSource())){
                        throw exception(ErrorCodeConstants.GOMall_Query_Error, id + "自建计划合同不用推送监管备案");
                    }
                }
            }else{
                List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(AssociatedPlanDO::getOrderId, contractOrderRelDOList.get(0).getOrderId());
                if (ObjectUtil.isEmpty(associatedPlanDOS)) {
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, id + "无计划合同不用推送监管备案");
                }
                if("0".equals(associatedPlanDOS.get(0).getBuyPlanSource())){
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, id + "自建计划合同不用推送监管备案");
                }
            }
        }
//        ContractDO contractDO = new ContractDO();
//        contractDO.setId(id);
//        contractDO.setIsFilings(1);
//        contractMapper.updateById(contractDO);
//        //合同状态同步给黑龙江
//        updateStatus(id);
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            if (!PlatformEnums.JDMALL.getCode().equals(orderContractDO.getPlatform()) && !PlatformEnums.ZHUBAJIE.getCode().equals(orderContractDO.getPlatform())) {
                System.out.println("【合同备案】合同备案platform:" + orderContractDO.getPlatform() + "id:" + id);
                FileDTO fileDTO = fileApi.selectById(orderContractDO.getPdfFileId());
                File file = downloadFileToTemp(fileDTO);
                MultipartFile multipartFile = createMultipartFile(file);
                contractBak2(orderContractDO, multipartFile, request);
            } else {
                contractBak2(orderContractDO, null, request);
            }
        } catch (Exception e) {
            log.error("【合同备案】合同备案失败,id---->" + id, e.getMessage());
            e.printStackTrace();
            //throw new Exception(" ");
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, id + "  备案失败:" + e.getMessage());
        }
    }

    private void updateStatus(String id) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            try {
                throw new Exception(jsonObject.getString("error_description"));
            } catch (Exception e) {
                throw new RuntimeException(jsonObject.getString("error_description"));
            }
        }
        try {
            SyncContractStatusDTO dto = new SyncContractStatusDTO()
                    .setContractList(Collections.singletonList(
                            new ContractStatusDTO()
                                    .setContractGuid(id)
                                    .setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())
                                    .setOperatorStr(adminUserApi.getUser(getLoginUserId()).getNickname())
                    ));
            String result =  contractProcessApi.unitSyncContractStatus(jsonObject.getString("access_token"),dto);
            System.out.println(result);
            JSONObject resultJson = JSONObject.parseObject(result);
            if (!"0".equals(resultJson.getString("code"))) {
                throw new RuntimeException("同步状态失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("同步状态失败");
        }
    }


    public void contractBak2(ContractOrderExtDO contract, MultipartFile file, HttpServletRequest request) throws Exception {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
            ContractOrderExtDO orderContractDO = contract;
            ContractOrderExtDO tradingContractDO = contract;
            if (1 == orderContractDO.getModify()) {
                //合同已锁定   不可修改合同
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同已被锁定，暂不可操作合同");
            }
            //获取配置判断是否走融通平台接口进行备案
            String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
            ContractVo contractVo = OrderContractConverter.INSTANCE.convert2Vo(orderContractDO);
            //获取采购合同履约验收要求列表---货物类合同必填
            this.contractAcceptanceProcessor(contractVo);
            if (ObjectUtil.isNotEmpty(file) && (!PlatformEnums.JDMALL.getCode().equals(orderContractDO.getPlatform()) &&
                    !PlatformEnums.ZHUBAJIE.getCode().equals(orderContractDO.getPlatform())) && !"PICS".equals(tradingContractDO.getSourceCode())) {
                FileAcceptDTO fileAcceptDTO = uploaderFile(file, accessToken, request, orderContractDO.getPlatform(), orderContractDO.getId());
                System.out.println("【合同备案】合同备案contractBak2,上传文件完成");
                ContractAttachmentVo contractAttachmentVo = new ContractAttachmentVo().setAttachmentType(AttachmentTypeEnums.MAIN.getCode())
                        .setFileName(fileAcceptDTO.getName()).setFilePath(fileAcceptDTO.getFileId());
                List<ContractAttachmentVo> contractAttachmentVos = new ArrayList<>();
                contractAttachmentVos.add(contractAttachmentVo);
                contractVo.setAttachmentList(contractAttachmentVos);
            }
            //上传交易补充协议
            if (PlatformEnums.GPMS_GPX == PlatformEnums.getInstance(orderContractDO.getPlatform())) {
                List<ContractFileDO> contractFileDOS = contractFileMapper.selectList(new LambdaQueryWrapperX<ContractFileDO>()
                        .eq(ContractFileDO::getContractId, contract.getId())
                        .eq(ContractFileDO::getAttachmentType, AttachmentTypeEnums.OTHER.getCode())
                );
                if (CollectionUtil.isNotEmpty(contractFileDOS)) {
                    List<Long> collect = contractFileDOS.stream().map(ContractFileDO::getFileId).collect(Collectors.toList());
                    for (Long aLong : collect) {
                        FileDTO fileDTO = fileApi.selectById(aLong);
                        File file1 = downloadFileToTemp(fileDTO);
                        MultipartFile multipartFile1 = createMultipartFile(file1);
                        FileAcceptDTO fileAcceptDTO = uploaderFile(multipartFile1, accessToken, request, orderContractDO.getPlatform(), orderContractDO.getId());
                        ContractAttachmentVo contractAttachmentVo = new ContractAttachmentVo().setAttachmentType(AttachmentTypeEnums.OTHER.getCode()).setFileName(fileAcceptDTO.getName()).setFilePath(fileAcceptDTO.getFileId());
                        List<ContractAttachmentVo> contractAttachmentVos = new ArrayList<>();
                        contractAttachmentVos.add(contractAttachmentVo);
                        contractVo.getAttachmentList().addAll(contractAttachmentVos);
                    }
                }
            }
            if (PlatformEnums.JDMALL.getCode().equals(orderContractDO.getPlatform()) || PlatformEnums.ZHUBAJIE.getCode().equals(orderContractDO.getPlatform())) {
                FileDTO fileDTO = fileApi.selectById(orderContractDO.getPdfFileId());
                ArrayList<String> keyList = new ArrayList<>();
                keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
                List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
                String prefix = configsByCKeys.get(0).getCValue();
                String path = fileDTO == null ? null : fileDTO.getPath();
                String url = prefix + path;
                ContractAttachmentVo contractAttachmentVo = new ContractAttachmentVo().setAttachmentType(AttachmentTypeEnums.MAIN.getCode()).setFileName(fileDTO == null ? null : fileDTO.getName()).setFilePath(url);
                List<ContractAttachmentVo> contractAttachmentVos = new ArrayList<>();
                contractAttachmentVos.add(contractAttachmentVo);

                //添加补充协议
                contractFileMapper.selectList(ContractFileDO::getContractId, contract.getId()).forEach(contractFileDO -> {
                    ContractAttachmentVo attachmentVo = new ContractAttachmentVo();
                    attachmentVo.setAttachmentType(contractFileDO.getAttachmentType());
                    attachmentVo.setFileName(contractFileDO.getFileName());
                    attachmentVo.setFilePath(contractFileDO.getFileUrl());
                    contractAttachmentVos.add(attachmentVo);
                });
                contractVo.setAttachmentList(contractAttachmentVos);
            }
            if (ObjectUtil.isEmpty(contractVo.getSupplierFeatures())) {
                contractVo.setSupplierFeatures(9);
            }
            this.contractBasicProcessor(contractVo, orderContractDO);
//
//        this.contractFilesProcessor(contractVo);
            //补全信息----电子交易
            if (PlatformEnums.GPMS_GPX == PlatformEnums.getInstance(orderContractDO.getPlatform())) {

                System.out.println("【合同备案】合同备案contractBak2,执行电子交易备案id : " + contract.getId());
                //电子交易实体类
                GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, tradingContractDO.getProjectGuid());
//            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, tradingContractDO.getBidGuid());
//            packageInfoDO.getSupplierIds();
//                List<SupplierInfoDO> supplierInfoDOList = supplierInfoMapper.selectList(SupplierInfoDO::getPackageId, tradingContractDO.getBidGuid());
//                SupplierInfoDO supplierInfoDO = supplierInfoDOList.get(0);

                List<GPXContractQuotationRelDO> contractQuotationRelDOList = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, contract.getId());
                Map<String, GPXContractQuotationRelDO> contractQuotationRelDOMap = new HashMap<String, GPXContractQuotationRelDO>();
                if(CollectionUtil.isNotEmpty(contractQuotationRelDOList)){
                    contractQuotationRelDOMap= CollectionUtils.convertMap(contractQuotationRelDOList,GPXContractQuotationRelDO::getPackageDetailId);
                }
                //包详情list
                List<PackageDetailInfoDO> packageDetailInfoDOS = new ArrayList<>();
                if (UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
                    packageDetailInfoDOS = packageDetailInfoMapper.getPackageDetailList4Union(contract.getId());
                } else {
                    packageDetailInfoDOS = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, tradingContractDO.getBidGuid());

                }
//            List<PackageDetailInfoDO> packageDetailInfoDOS = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, tradingContractDO.getBidGuid());


                List<ContractBillVo> contractBillVos2 = convertToContractBillVoListV1(packageDetailInfoDOS, contract,
                        projectDO, contractQuotationRelDOMap);
                if(ObjectUtil.isNotEmpty(contractBillVos2)&&contractBillVos2.size()==1){
                    contractBillVos2.get(0).setTotalPrice(contractVo.getTotalMoney());
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
                contractVo.setItemList(contractBillVos2);

                if (ObjectUtil.isNull(projectDO)) {
                    throw exception(EMPTY_DATA_ERROR_V2, "采购项目");
                }
                if (ObjectUtil.isEmpty(tradingContractDO)) {
                    throw exception(EMPTY_DATA_ERROR_V2, "合同" + contract.getId());
                }
                if (ObjectUtil.isEmpty(contractVo.getContractPayType())) {
                    if (ObjectUtil.isNotEmpty(tradingContractDO.getContractPayType())) {
                        contractVo.setContractPayType(tradingContractDO.getContractPayType());
                    } else {
                        contractVo.setContractPayType(1);
                    }
                }
                if (ObjectUtil.isEmpty(contractVo.getMultiAccountPayType())) {
                    contractVo.setMultiAccountPayType("0");
                }
                if (ObjectUtil.isEmpty(contractVo.getBidOpenTime())) {
                    if (ObjectUtil.isNotEmpty(projectDO.getBidOpenTime())) {
                        contractVo.setBidOpenTime(projectDO.getBidOpenTime());
                    } else {
                        contractVo.setBidOpenTime(new Date());
                    }
                }
                if (ObjectUtil.isEmpty(contractVo.getBidResultDate())) {
                    contractVo.setBidResultDate(new Date());
                }
                //供应商信息
                List<TradingSupplierDO> contractSupplierDOS = tradingSupplierMapper.selectList(TradingSupplierDO::getContractId, orderContractDO.getId());
                List<ContractSupplierVo> contractSupplierVos = TradingSupplierConverter.INSTANCE.convertLsitV2(contractSupplierDOS);
                SupplyDTO supply = supplyApi.getSupply(orderContractDO.getSupplierId());
                if(ObjectUtil.isNull(supply)){
                    supply = hljSupplyService.getSupply(orderContractDO.getSupplierId());
                }
                if (StringUtils.isEmpty(contractVo.getSupplierName())) {
                    contractVo.setSupplierName(supply.getSupplyCn());
                }
                if(ObjectUtil.isNotEmpty(contractSupplierDOS) && StringUtils.isEmpty(contractVo.getSupplierTelphone())){
                    contractVo.setSupplierTelphone(contractSupplierDOS.get(0).getSupplierLinkMobile());
                }
                for (ContractSupplierVo contractSupplierVo : contractSupplierVos) {
                    if (ObjectUtil.isEmpty(contractSupplierVo.getPayee())) {
                        contractSupplierVo.setPayee(0);
                    }
                    contractSupplierVo.setForeignInvestmentType(null);
                    if (ObjectUtils.isNotEmpty(supply)) {
                        setSupplierInfo( contractSupplierVo, supply, contractVo);
                    }
                }
                contractSupplierVos.get(0).setPayee(1);
                contractVo.setSupplierList(contractSupplierVos);
                //合同总金额
                if (ObjectUtil.isEmpty(tradingContractDO.getTotalMoney())) {
                    throw exception(EMPTY_DATA_ERROR_V2, "合同总金额");
                } else {
                    contractVo.setTotalMoney(tradingContractDO.getTotalMoney().doubleValue());
                }
                //履约开始日期
                if (ObjectUtil.isNotEmpty(tradingContractDO.getPerformStartDate())) {
                    contractVo.setPerformStartDate(tradingContractDO.getPerformStartDate());
                }
                //履约结束日期
                if (ObjectUtil.isNotEmpty(tradingContractDO.getPerformEndDate())) {
                    contractVo.setPerformEndDate(tradingContractDO.getPerformEndDate());
                }
                //供应商联系地址
                if (ObjectUtil.isEmpty(contractSupplierDOS.get(0).getRegisteredAddress())) {
                    contractVo.setSupplierAddress("1");
                } else {
                    contractVo.setSupplierAddress(contractSupplierDOS.get(0).getRegisteredAddress());
                }
                //采购项目
//            GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, tradingContractDO.getProjectGuid());
                //采购项目Guid
                if (ObjectUtil.isEmpty(projectDO) || ObjectUtil.isEmpty(projectDO.getProjectGuid())) {
//                contractVo.setProjectGuid("1");
                } else {
                    contractVo.setProjectGuid(projectDO.getProjectGuid());
                }
                //项目名称
                if (ObjectUtil.isEmpty(projectDO) || ObjectUtil.isEmpty(projectDO.getProjectName())) {
//                contractVo.setProjectName("1");
                } else {
                    contractVo.setProjectName(projectDO.getProjectName());
                }

                //采购单位联系人
                if (ObjectUtil.isEmpty(contractVo.getOrgLinkman())) {
                    contractVo.setOrgLinkman(tradingContractDO.getBuyerLink());
                }
                //采购人联系方式
                if (ObjectUtil.isEmpty(contractVo.getOrgTelphone())) {
                    contractVo.setOrgTelphone(tradingContractDO.getBuyerPhone());
                }
                //采购人地址
                if (ObjectUtil.isEmpty(contractVo.getOrgAddress())) {
                    contractVo.setOrgAddress(tradingContractDO.getOrgAddress());
                }
                OrganizationDTO organization = organizationApi.getOrganization(tradingContractDO.getBuyerOrgId());
                if (StringUtils.isEmpty(contractVo.getOrgAddress())) {
                    //设置采购人信息
                    contractVo.setOrgAddress(organization == null ? null : organization.getAddress());
                }
                if (StringUtils.isEmpty(contractVo.getOrgLinkman())) {
                    //设置采购人信息
                    contractVo.setOrgLinkman(organization == null ? null : organization.getLegal());
                }
                if (StringUtils.isEmpty(contractVo.getOrgTelphone())) {
                    //设置采购人信息
                    contractVo.setOrgTelphone(organization == null ? null : organization.getLinkPhone());
                }
                //项目编号

                if (ObjectUtil.isEmpty(projectDO) || ObjectUtil.isEmpty(projectDO.getProjectCode())) {
                    contractVo.setProjectCode("1");
                } else {
                    contractVo.setProjectCode(projectDO.getProjectCode());
                }
                //采购计划唯一识别码
//            contractVo.setBuyPlanGuid(tradingContractDO.getBuyPlanId());
                //过滤掉计划的tag
                PlanInfoDO planInfo = planInfoMapper.selectOne(PlanInfoDO::getPlanId, tradingContractDO.getBuyPlanId());
                if (ObjectUtil.isNotEmpty(planInfo)) {
                    contractVo.setBuyPlanGuid(planInfo.getOutSitePlanId());
                } else {
                    String planId = EcontractUtil.cleanTag(tradingContractDO.getBuyPlanId());
                    contractVo.setBuyPlanGuid(planId);
                }
                //供应商是否收款人
                PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, tradingContractDO.getBuyPlanPackageId());
                //供应商规模
                if (ObjectUtil.isEmpty(contractVo.getSupplierSize())) {
                    contractVo.setSupplierSize(1);
                }
                //采购方式
                contractVo.setPurMethod(packageInfoDO.getPurchaseMethodCode());
                //签署地址
                if (ObjectUtil.isEmpty(orderContractDO.getContractSignAddress())) {
                    contractVo.setSignAddress(tradingContractDO.getContractSignAddress());
                }
                if (ObjectUtil.isEmpty(orderContractDO.getContractSignAddress())) {
                    contractVo.setSignAddress("合同签订地址");
                }
                /**
                 *
                 * GPMS_PSP   采购单位服务平台
                 * PICS    医疗采购
                 * GPMS   监管
                 * SPEEDIT 高校
                 */
                //电子交易推送--监管
                this.contractPaymentPlanProcessor(contractVo);
                //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
                if ("y".equals(cValue)) {
                    String orgGuid = tradingContractDO.getBuyerOrgId();
                    String regionCode = "";
                    if (ObjectUtil.isNotNull(organization)) {
                        RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                        regionCode = region.getRegionCode();
                    }
                    if ("GPMS_PSP".equals(tradingContractDO.getSourceCode())) {
                        //采购单位服务平台
                        //合同采购计划明细
                        List<ContractSupplierVo> supplierList = contractVo.getSupplierList();
                        contractVo.setSupplierLocation(supplierList.get(0).getSupplierLocation());
                        PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, tradingContractDO.getBuyPlanId());
                        contractVo.setBuyPlanGuid(planInfoDO.getOutSitePlanId());
                    }
                    if ("PICS".equals(tradingContractDO.getSourceCode())) {
                        //医疗
                        PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, tradingContractDO.getBuyPlanId());
                        contractVo.setBuyPlanGuid(planInfoDO.getOutSitePlanId());
                    }
                    EncryptResponseDto response = outOpenApiService.setContractV2(accessToken, orgGuid, regionCode, Sm4Utils.convertParam(contractVo));
                    //备案结果判断
                    resulJudgment(response, contractVo, orderContractDO);
                } else {
                    if ("GPMS".equals(tradingContractDO.getSourceCode())) {
                        System.out.println("【电子交易合同备案】projectId：" + projectDO.getProjectGuid());
                        //判断采购类型
                        if (ObjectUtil.isNotEmpty(projectDO) && ObjectUtil.isNotEmpty(projectDO.getBiddingMethodCode())) {
                            //批量集中采购
                            if (BiddingMethodEnums.BATCH.getCode().equals(projectDO.getBiddingMethodCode())) {
                                System.out.println("【电子交易合同备案】执行批量集中采购备案|请求参数 contractVo:" + JsonUtils.toJsonString(contractVo));
                                System.out.println("【电子交易合同备案】执行一般项目采购备案|请求加密参数 accessToken:" + accessToken + "," + "EncryptDTO:" + JsonUtils.toJsonString(Sm4Utils.convertParam(contractVo)));
                                EncryptResponseDto response = outOpenApiService.gpxbSetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                System.out.println("【电子交易合同备案】执行批量集中采购备案|返回数据 response:" + JsonUtils.toJsonString(response));

                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            } else {
                                //一般项目采购 (BiddingMethodEnums.COMMON.getCode().equals(projectDO.getBiddingMethodCode()))
                                System.out.println("【电子交易合同备案】执行一般项目采购备案|请求参数 contractVo:" + JsonUtils.toJsonString(contractVo));
                                System.out.println("【电子交易合同备案】执行一般项目采购备案|请求加密参数 accessToken:" + accessToken + "," + "EncryptDTO:" + JSONObject.toJSONString(Sm4Utils.convertParam(contractVo)));
                                EncryptResponseDto response = outOpenApiService.gpxSetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                System.out.println("【电子交易合同备案】执行一般项目采购备案|返回数据 response:" + JsonUtils.toJsonString(response));
                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            }
                        }
                    } else if ("GPMS_PSP".equals(tradingContractDO.getSourceCode())) {
                        //采购单位服务平台
                        //合同采购计划明细
                        List<ContractSupplierVo> supplierList = contractVo.getSupplierList();
                        contractVo.setSupplierLocation(supplierList.get(0).getSupplierLocation());
                        PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, tradingContractDO.getBuyPlanId());
                        contractVo.setBuyPlanGuid(planInfoDO.getOutSitePlanId());
                        //判断采购类型
                        if (ObjectUtil.isNotEmpty(projectDO) && ObjectUtil.isNotEmpty(projectDO.getBiddingMethodCode())) {
                            //批量集中采购
                            if (BiddingMethodEnums.BATCH.getCode().equals(projectDO.getBiddingMethodCode())) {
                                EncryptResponseDto response = outOpenApiService.gpxbOrgSetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            } else {
                                //一般项目采购 (BiddingMethodEnums.COMMON.getCode().equals(projectDO.getBiddingMethodCode()))
                                EncryptResponseDto response = outOpenApiService.gpxOrgSetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            }
                        }
                    } else if ("SPEEDIT".equals(tradingContractDO.getSourceCode())) {
                        //高校
                        //判断采购类型
                        if (ObjectUtil.isNotEmpty(projectDO) && ObjectUtil.isNotEmpty(projectDO.getBiddingMethodCode())) {
                            //批量集中采购
                            if (BiddingMethodEnums.BATCH.getCode().equals(projectDO.getBiddingMethodCode())) {
                                EncryptResponseDto response = outOpenApiService.gpxbUniversitySetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            } else {
                                //一般项目采购 (BiddingMethodEnums.COMMON.getCode().equals(projectDO.getBiddingMethodCode()))
                                EncryptResponseDto response = outOpenApiService.gpxUniversitySetContract(accessToken, Sm4Utils.convertParam(contractVo));
                                //备案结果判断
                                resulJudgment(response, contractVo, orderContractDO);
                            }
                        }
                    } else if ("PICS".equals(tradingContractDO.getSourceCode())) {
                        //医疗
                        PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, tradingContractDO.getBuyPlanId());
                        contractVo.setBuyPlanGuid(planInfoDO.getOutSitePlanId());
                        //获取医疗token
                        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
                        tokenRequestDTO.setUsername("241777");
                        tokenRequestDTO.setNoise(System.currentTimeMillis() + "");
                        String sign = calculateSign(tokenRequestDTO.getNoise(), "teamlead");
                        tokenRequestDTO.setSign(sign);
                        String jsonString = outOpenApiService.getToken(tokenRequestDTO);
                        JSONObject jsonObject = JSONObject.parseObject(jsonString);
                        // 从data对象中获取access_token
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        String accessToken1 = dataObject.getString("access_token");
                        // 设置请求头，添加"Bearer "前缀
                        String authorizationHeader = "Bearer " + accessToken1;
                        //数据结构 buyPlanGuid 改成 buyplanGuid，他们之前是按buyplanGuid对接的
                        ContractMVO mVo = OrderContractConverter.INSTANCE.convertV2M(contractVo);
                        String response = outOpenApiService.setMedicalContract(authorizationHeader, mVo);
                        //备案结果判断
                        System.out.println(response);
//                resulJudgment(response, contractVo, orderContractDO);
                        ResponseDTO responseDTO = JSONObject.parseObject(response, ResponseDTO.class);
                        //直接将合同改成备案中
                        if ("0".equals(responseDTO.getStatus())) {
                            orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
                            contractOrderExtMapper.updateById(orderContractDO);
                            //数据统计表修改合同状态
                            LambdaUpdateWrapper<StatisticsDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.set(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
                            lambdaUpdateWrapper.eq(StatisticsDO::getContractId, orderContractDO.getId());
                            statisticsMapper.update(null,lambdaUpdateWrapper);
                        } else {
                            throw exception(ErrorCodeConstants.CONTRACT_BACK_ERROR, "合同备案失败");
                        }

                    } else {
                        throw exception(EMPTY_DATA_ERROR_V2, "数据来源");
                    }
                }
                //-----------------------------------------------------电子交易结尾-----------------------------------------------------
            } else {
                //其他场景-供应商信息
                this.contractSupplierProcessor(contractVo, orderContractDO);
                this.contractBillsProcessor(contractVo, orderContractDO);
                this.contractPaymentPlanProcessor(contractVo);
                OrganizationDTO organization = organizationApi.getOrganization(orderContractDO.getBuyerOrgId());
                if (StringUtils.isEmpty(contractVo.getOrgAddress())) {
                    //设置采购人信息
                    contractVo.setOrgAddress(organization == null ? null : organization.getAddress());
                }
                if (StringUtils.isEmpty(contractVo.getOrgLinkman())) {
                    //设置采购人信息
                    contractVo.setOrgLinkman(organization == null ? null : organization.getLegal());
                }
                if (StringUtils.isEmpty(contractVo.getOrgTelphone())) {
                    //设置采购人信息
                    contractVo.setOrgTelphone(organization == null ? null : organization.getLinkPhone());
                }
                RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
                EncryptResponseDto response = null;
                //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
                if ("y".equals(cValue)) {
                    String orgGuid = orderContractDO.getBuyerOrgId();
                    String regionCode = "";
                    if (ObjectUtil.isNotNull(organization)) {
                        RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                        regionCode = region.getRegionCode();
                    }
                    response = outOpenApiService.setContractV2(accessToken, orgGuid, regionCode, Sm4Utils.convertParam(contractVo));
                } else {
                    response = outOpenApiService.setContract(accessToken, Sm4Utils.convertParam(contractVo));
                }
                //备案结果判断
                resulJudgment(response, contractVo, orderContractDO);
            }
        }

        private void setSupplierInfo(ContractSupplierVo contractSupplierVo,SupplyDTO supply,ContractVo contractVo) {
            contractSupplierVo.setSupplierCode(supply.getOrgCode());
            if (ObjectUtil.isEmpty(contractSupplierVo.getBankAccount())) {
                contractSupplierVo.setBankAccount(supply.getBankAccount());
            }
            if (ObjectUtil.isEmpty(contractSupplierVo.getBankName())) {
                contractSupplierVo.setBankName(supply.getBankName());
            }
            if (StringUtils.isEmpty(contractVo.getSupplierFax())) {
                contractVo.setSupplierFax(supply.getFax());
            }
            if (StringUtils.isEmpty(contractVo.getSupplierAddress())) {
                contractVo.setSupplierAddress(supply.getAddr());
            }
            if (StringUtils.isEmpty(contractVo.getSupplierLocation())) {
                contractVo.setSupplierLocation(supply.getReginCode() == null ? "" : supply.getReginCode());
            }
            if (StringUtils.isEmpty(contractSupplierVo.getSupplierLocation())) {
                contractSupplierVo.setSupplierLocation(supply.getReginCode() == null ? "" : supply.getReginCode());
            }
            if (StringUtils.isEmpty(contractVo.getZoneCode())) {
                contractVo.setZoneCode(supply.getReginCode() == null ? "" : supply.getReginCode());
            }
            if (StringUtils.isEmpty(contractSupplierVo.getZoneCode())) {
                contractSupplierVo.setZoneCode(supply.getReginCode() == null ? "" : supply.getReginCode());
            }
            if (StringUtils.isEmpty(contractVo.getSupplierProxy())) {
                contractVo.setSupplierProxy(supply.getPersonName());
            }
            if (StringUtils.isEmpty(contractVo.getSupplierTelphone())) {
                contractVo.setSupplierTelphone(supply.getTel());
            }
            if (ObjectUtil.isEmpty(contractVo.getSupplierSize())) {
                contractVo.setSupplierSize(supply.getUnitScopeCode() == null ? SupplierSizeEnum.OTHER.getCode() : (Integer.valueOf(setSupplierSize(supply.getUnitScopeCode()))));
            }
            if (ObjectUtil.isEmpty(contractSupplierVo.getSupplierSize())) {
                contractSupplierVo.setSupplierSize(supply.getUnitScopeCode() == null ? SupplierSizeEnum.OTHER.getCode() : (Integer.valueOf(setSupplierSize(supply.getUnitScopeCode()))));
            }
            if (ObjectUtil.isEmpty(contractSupplierVo.getSupplierFeatures())) {
                contractSupplierVo.setSupplierFeatures(contractVo.getSupplierFeatures());
            }
            if (ObjectUtil.isEmpty(contractSupplierVo.getAccountName())) {
                contractSupplierVo.setAccountName(supply.getSupplyCn());
            }
            if (ObjectUtil.isEmpty(contractSupplierVo.getBankNO())) {
                contractSupplierVo.setActualAccountName(supply.getBankName());
            }
            contractSupplierVo.setTotalMoney(contractVo.getTotalMoney());
        }
        private String getCvalueByKey(String key) {
            ArrayList<String> keyList = new ArrayList<>();
            keyList.add(key);
            List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
            String cValue = configsByCKeys.size()==0?null:configsByCKeys.get(0).getCValue();
            return cValue;
        }

        /**
         * 根据一般项目或者联合采购，获取计划id
         */
//    private ContractVo enhancePlanId(ContractVo contractVo) {
//        String planId = contractVo.getBuyPlanGuid();
//        //一般项目计划外部id
//        PlanInfoDO planInfoDO = planInfoMapper.selectById(planId);
//        if (ObjectUtil.isNotNull(planInfoDO)) {
//            contractVo.setBuyPlanGuid(planInfoDO.getOutSitePlanId());
//        } else {
//            //联合采购的计划外部id
//            BatchPlanInfoDO batchPlanInfoDO = batchPlanInfoMapper.selectOne(BatchPlanInfoDO::getPlanId, planId);
//            if (ObjectUtil.isNotNull(batchPlanInfoDO)) {
//                contractVo.setBuyPlanGuid(batchPlanInfoDO.getOutSitePlanId());
//            }
//        }
//        return contractVo;
//    }
        private String setSupplierSize(String supplierSize) {
            /**
             * 0 大型企业
             * 4 微型企业
             * 1 中型企业
             * 2小型企业
             * 3 其他
             */
            switch (supplierSize) {
                case "0":
                    return SupplierSizeEnum.MAJOR_INDUSTRY.getCode().toString();
                case "1":
                    return SupplierSizeEnum.MEDIUM_ENTERPRISE.getCode().toString();
                case "2":
                    return SupplierSizeEnum.SMALL_ENTERPRISE.getCode().toString();
                case "3":
                    return SupplierSizeEnum.OTHER.getCode().toString();
                case "4":
                    return SupplierSizeEnum.MICRO_ENTERPRISE.getCode().toString();
                default:
                    return SupplierSizeEnum.OTHER.getCode().toString();
            }
        }

        private String calculateSign(String noise, String key) {
            try {
                String toHash = noise + "##" + key;
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hash = md.digest(toHash.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        private void resulJudgment(EncryptResponseDto response, ContractVo contractVo, ContractOrderExtDO orderContractDO) throws
        JsonProcessingException {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(contractVo);
                if (!"0".equals(response.getStatus()) && ObjectUtil.isEmpty(response.getData()) && ObjectUtil.isNotEmpty(response.getErrorInfoDesc())) {
                    log.error(contractVo.getContractGuid() + "备案失败： " + response.getErrorInfoDesc() + "  备案合同数据： " + json);
                    throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, contractVo.getContractGuid() + "备案失败:" + response.getErrorInfoDesc());
                } else {
                    System.out.println(contractVo.getContractGuid() + "备案成功，合同数据： " + json);
                    orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
                    contractOrderExtMapper.updateById(orderContractDO);
                    System.out.println("修改合同状态成功:" + orderContractDO.getId());
                    //数据统计表修改合同状态
                    LambdaUpdateWrapper<StatisticsDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.set(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
                    lambdaUpdateWrapper.eq(StatisticsDO::getContractId, orderContractDO.getId());
                    statisticsMapper.update(null,lambdaUpdateWrapper);
//                    gPMallContractService.sendEcmsOperationLogEvent(orderContractDO, null, null);
                    System.out.println("触发操作日志监听成功:" + orderContractDO.getId());
                    //调平台接口推送合同信息
//                    contractPublisher.sendContractDataEvent(new ContractDOEvent(this).setContractId(orderContractDO.getId()));
                    System.out.println("触发平台推送监听成功:" + orderContractDO.getId());
                    ContractDO contractDO = new ContractDO();
                    contractDO.setId(orderContractDO.getId());
                    contractDO.setIsFilings(1);
                    contractMapper.updateById(contractDO);
                    //合同状态同步给黑龙江
                    updateStatus(orderContractDO.getId());
                }
            } catch (Exception e) {
                System.out.println("备案结果判断异常：" + contractVo.getContractGuid() + "e:" + e.getMessage());
                    throw e;
            }

    }
    private File downloadFileToTemp(FileDTO fileDTO) throws Exception {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String prefix = configsByCKeys.get(0).getCValue();
        String path = fileDTO == null ? null : fileDTO.getPath();
        String urlString = prefix + path;
        URL url = new URL(urlString);
        try (InputStream inputStream = url.openStream()) {
            // 创建一个临时文件夹
            Path tempDir = Files.createTempDirectory("tempFiles");
            // 目标路径
            Path tempFilePath = tempDir.resolve(fileDTO.getName());
            Files.copy(inputStream, tempFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return tempFilePath.toFile();
        }
    }
    /**
     * 处理合同支付计划
     *
     * @param contractVo 合同主体
     */
    public void contractAcceptanceProcessor(ContractVo contractVo) {
        String contractGuid = contractVo.getContractGuid();
        //查询合同绑定的参数信息（货物类合同履约验收要求信息）
        List<OrderContractParamFieldDO> orderContractParamFieldDOS = contractParamFieldMapper.selectList(new LambdaQueryWrapperX<OrderContractParamFieldDO>().eqIfPresent(OrderContractParamFieldDO::getContractId, contractGuid));
        if (CollectionUtil.isNotEmpty(orderContractParamFieldDOS)) {
            List<ContractAcceptanceVo> vos = ContractParamFieldConverter.INSTANCE.toVOS(orderContractParamFieldDOS);
            contractVo.setContractAcceptanceList(vos);
        }

    }

    public FileAcceptDTO uploaderFile(MultipartFile file, String accessToken, HttpServletRequest request, String platform, String contractId) throws Exception {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        EncryptResponseDto response = null;
        try {
            if (StringUtils.isEmpty(accessToken)) {
                accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
            }
            MultipartFile multipartFile = Sm4Utils.convertFile2(file);
            //获取配置判断是否走融通平台接口进行备案
            String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
            //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
            if ("y".equals(cValue)) {
                ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>()
                        .eqIfPresent(ContractOrderExtDO::getId, contractId).eqIfPresent(ContractOrderExtDO::getPlatform, platform)
                        .select(ContractOrderExtDO::getId, ContractOrderExtDO::getBuyerOrgId));
                if (ObjectUtil.isEmpty(orderContractDO)) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "合同不存在");
                }
                String orgGuid = orderContractDO.getBuyerOrgId();
                String regionCode = "";
                OrganizationDTO organization = organizationApi.getOrganization(orgGuid);
                if (ObjectUtil.isNotNull(organization)) {
                    RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                    regionCode = region.getRegionCode();
                }
                response = outOpenApiService.uploaderFileV2(accessToken, orgGuid, regionCode, multipartFile);
            } else {
                if (PlatformEnums.GPMS_GPX == PlatformEnums.getInstance(platform)) {
                    response = outOpenApiService.uploaderFileV1(accessToken, multipartFile);
                }
            }
            if (ObjectUtil.isEmpty(response.getData()) && ObjectUtil.isNotEmpty(response.getErrorInfoDesc())) {
                throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, "上传文件到监管失败" + response.getErrorInfoDesc());
            } else {
                FileAcceptDTO fileAcceptDTO = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), FileAcceptDTO.class);
                return fileAcceptDTO;
            }
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, "上传文件到监管失败" + e.getMessage());
        }
    }
        /**
         * 处理合同主体
         *
         * @param contractVo 合同主体
         */
        public void contractBasicProcessor(ContractVo contractVo, ContractOrderExtDO orderContractDO) {
            //2.设置合同支付类型---存在多笔支付计划的传分期付款/分期付款--0，一笔支付计划就传一次性付清--1
            Long count = paymentScheduleMapper.selectCount(PaymentScheduleDO::getContractId, contractVo.getContractGuid());
            if (count == 0) {
                //支付计划为空获取阶段支付信息
                count = paymentScheduleMapper.selectCount(PaymentScheduleDO::getContractId, contractVo.getContractGuid());
            }
            //2.1默认一次性付清
            if (ObjectUtil.isEmpty(contractVo.getContractPayType())) {
                contractVo.setContractPayType(1);
                if (count > 1) {
                    contractVo.setContractPayType(0);
                }
            }
            if (ObjectUtil.isEmpty(contractVo.getContractPayCount())) {
                contractVo.setContractPayCount(count.intValue());
                //3.设置支付计划的笔数
                if (count <= 0) {
                    contractVo.setContractPayCount(1);
                }
            }
            //4.设置合同多方支付方式---合同乙方中有多少个供应商是作为收款供应商的，一个收款供应商--0为只对单账户收款，2就是多账户收款  ---1为指定金额的多账户付款---2为不指定金额的多账户付款
            Long accountNum = tradingSupplierMapper.selectCount(new LambdaQueryWrapperX<TradingSupplierDO>().eq(TradingSupplierDO::getContractId, contractVo.getContractGuid())
                    .groupBy(TradingSupplierDO::getSupplierId));
            //4.1 默认一次性付清
            contractVo.setMultiAccountPayType("0");
            if (ObjectUtil.isNotEmpty(accountNum) && accountNum > 1) {
                //多个供应商收款账户
                contractVo.setMultiAccountPayType("1");
            }
            //5.设置项目id，项目名称，项目编码,采购方式，项目开标时间,中标(成交)日期
//            if (PlatformEnums.PROCESSLESS == PlatformEnums.getInstance(orderContractDO.getPlatform())) {
//                ContractProjectDO projectDO = contractProjectMapper.selectOne(ContractProjectDO::getContractId, contractVo.getContractGuid());
//                if (ObjectUtil.isNotEmpty(projectDO)) {
//                    contractVo.setProjectGuid(projectDO.getId());
//                    contractVo.setProjectCode(projectDO.getProjectCode());
//                    contractVo.setProjectName(projectDO.getProjectName());
//                    contractVo.setPurMethod(projectDO.getPurMethod());
////                contractVo.setBidOpenTime(projectDO.getBidOpenTime() != null ? projectDO.getBidOpenTime().getTime() : null);
////                contractVo.setBidResultDate(projectDO.getBidResultDate() != null ? projectDO.getBidResultDate().getTime() : null);
//                }
//            }
            //设置项目id，项目名称，项目编码,采购方式，项目开标时间,中标(成交)日期----电子交易
            if (PlatformEnums.GPMS_GPX == PlatformEnums.getInstance(orderContractDO.getPlatform())) {
                GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, contractVo.getProjectGuid());
                if (ObjectUtil.isNotEmpty(projectDO)) {
                    contractVo.setProjectGuid(projectDO.getProjectGuid());
                    contractVo.setProjectCode(projectDO.getProjectCode());
                    contractVo.setProjectName(projectDO.getProjectName());
                }
            }
//        if (PlatformEnums.GPMALL == PlatformEnums.getInstance(orderContractDO.getPlatform()) || PlatformEnums.GP_GPFA == PlatformEnums.getInstance(orderContractDO.getPlatform())) {
            ObjectMapper objectMapper = new ObjectMapper();
            ProjectDO projectDO = gpMallProjectMapper.selectOne(ProjectDO::getOrderId, orderContractDO.getOrderId());
            DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(DraftOrderInfoDO::getOrderGuid, orderContractDO.getOrderId());
            try {
                System.out.println("查询的订单信息：" + objectMapper.writeValueAsString(draftOrderInfoDO));
            } catch (JsonProcessingException e) {
            }
            if (ObjectUtil.isNotEmpty(draftOrderInfoDO)) {
                if (StringUtils.isEmpty(contractVo.getBidGuid())) {
                    contractVo.setBidGuid(draftOrderInfoDO.getOrderGuid());
                }
                if (ObjectUtil.isEmpty(contractVo.getBidResultDate())) {
                    try {
                        contractVo.setBidResultDate(setDate(draftOrderInfoDO.getOrderCreateTime() == null ? draftOrderInfoDO.getPayTime() : draftOrderInfoDO.getOrderCreateTime()));
                    } catch (Exception e) {
                        LocalDateTime payTime = draftOrderInfoDO.getOrderCreateTime() == null ? draftOrderInfoDO.getPayTime() : draftOrderInfoDO.getOrderCreateTime();
                        Date date = payTime == null ? null : Date.from(payTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
                        contractVo.setBidResultDate(date);
                    }
                }
                if (StringUtils.isEmpty(contractVo.getBidCode())) {
                    contractVo.setBidCode(draftOrderInfoDO.getOrderCode());
                }
                if (ObjectUtil.isEmpty(contractVo.getBidOpenTime())) {
                    try {
                        contractVo.setBidOpenTime(setDate(draftOrderInfoDO.getPayTime() == null ? draftOrderInfoDO.getOrderCreateTime() : draftOrderInfoDO.getPayTime()));
                    } catch (Exception e) {
                        LocalDateTime payTime = draftOrderInfoDO.getPayTime() == null ? draftOrderInfoDO.getOrderCreateTime() : draftOrderInfoDO.getPayTime();
                        Date date = payTime == null ? null : Date.from(payTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
                        contractVo.setBidOpenTime(date);
                    }
                }
            }
            if (PlatformEnums.ZHUBAJIE.getCode().equals(contractVo.getPlatform())) {
                //猪八戒取实际的项目开标时间，中标成交时间
                ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(ContractInfoBackupsDO::getOrderId, orderContractDO.getOrderId());
                if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
                    String contractInfo = contractInfoBackupsDO.getContractInfo();
                    ContractDataDTO contractDataDTO = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
                    if (ObjectUtils.isNotEmpty(contractDataDTO)) {
                        String bidResultDateStr = contractDataDTO.getContractDTO() == null ? null : contractDataDTO.getContractDTO().getBidResultDate();
                        String bidOpenTimeStr = contractDataDTO.getContractDTO() == null ? null : contractDataDTO.getContractDTO().getBidOpenTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                        try {
                            if (StringUtils.isNotBlank(bidResultDateStr)) {
                                System.out.println("中标时间---->" + bidResultDateStr);
                                contractVo.setBidResultDate(simpleDateFormat.parse(bidResultDateStr));
                                System.out.println("转换后中标时间---->" + contractVo.getBidResultDate());
                            }
                        } catch (Exception e) {
                            contractVo.setBidResultDate(bidResultDateStr == null ? null : DateUtil.parse(bidResultDateStr).setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")));
                        }
                        try {
                            if (StringUtils.isNotBlank(bidOpenTimeStr)) {
                                System.out.println("开标时间---->" + bidOpenTimeStr);
                                contractVo.setBidOpenTime(simpleDateFormat.parse(bidOpenTimeStr));
                                System.out.println("转换后开标时间---->" + contractVo.getBidOpenTime());
                            }
                        } catch (Exception e) {
                            contractVo.setBidOpenTime(bidOpenTimeStr == null ? null : DateUtil.parse(bidOpenTimeStr).setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")));
                        }
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(projectDO)) {
                contractVo.setProjectGuid(projectDO.getId());
                contractVo.setProjectCode(projectDO.getProjectCode());
                contractVo.setProjectName(projectDO.getProjectName());
//            contractVo.setBidOpenTime();
//                contractVo.setBidResultDate(draftOrderInfoDO == null ? null : draftOrderInfoDO.getPayTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
            } else {
                if(StringUtils.isEmpty(contractVo.getProjectGuid())){
                    contractVo.setProjectGuid(draftOrderInfoDO == null ? null : draftOrderInfoDO.getOrderGuid());
                }
                if(StringUtils.isEmpty(contractVo.getProjectName())){
                    contractVo.setProjectName(draftOrderInfoDO == null ? null : draftOrderInfoDO.getPurchaserOrg() + "采购订单");
                }
                if(StringUtils.isEmpty(contractVo.getProjectCode())){
                    contractVo.setProjectCode(draftOrderInfoDO == null ? null : draftOrderInfoDO.getOrderCode());
                }
            }
            AssociatedPlanDO associatedPlanDO = associatedPlanMapper.selectOne(AssociatedPlanDO::getOrderId, orderContractDO.getOrderId());
            if (ObjectUtil.isNotEmpty(associatedPlanDO)) {
                //设置采购计划唯一识别码
                contractVo.setBuyPlanGuid(associatedPlanDO.getBuyPlanId());
                contractVo.setPurMethod(associatedPlanDO.getPurchaseMethod());
                contractVo.setSecret(associatedPlanDO.getSecret());
            }
//        }
            //
            //6.设置是否政府采购融资抵押(1:是,0:否)--默认为否
            contractVo.setAllowMortgage(ObjectUtil.isNotEmpty(orderContractDO.getAllowMortgage()) ? orderContractDO.getAllowMortgage() : "0");
            contractVo.setClauseSecret(ObjectUtil.isNotEmpty(orderContractDO.getClauseSecret()) ? orderContractDO.getClauseSecret() : 0);
            contractVo.setIsPerformanceMoney(ObjectUtil.isNotEmpty(orderContractDO.getIsPerformanceMoney()) ? orderContractDO.getIsPerformanceMoney() : 0);
            contractVo.setIsRetentionMoney(ObjectUtil.isNotEmpty(orderContractDO.getIsRetentionMoney()) ? orderContractDO.getIsRetentionMoney() : 0);
        }

    public List<ContractBillVo> convertToContractBillVoListV1(List<PackageDetailInfoDO> packageDetailInfoDOList, ContractOrderExtDO contract, GPXProjectDO projectDO,  Map<String, GPXContractQuotationRelDO> contractQuotationRelDOMap) {
        if(UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
            return packageDetailInfoDOList.stream()
                    .map(detail -> {
                        ContractBillVo contractBillVo = new ContractBillVo();
                        //合同明细唯一识别码设置uuid
                        contractBillVo.setContractBillGuid(UUID.randomUUID().toString().replace("-", ""));
                        //计划明细表
                        GPXContractQuotationRelDO contractQuotationRelDO=contractQuotationRelDOMap.get(detail.getDetailId());
                        if(ObjectUtil.isNotNull(contractQuotationRelDO)){
                            contractBillVo.setTotalPrice( Double.parseDouble(contractQuotationRelDO.getTotalPrice()));
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
        Boolean isOneSup= true;
        if (ObjectUtil.isNotNull(contract)) {
            PackageInfoDO packageInfoDO=packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid,contract.getBuyPlanPackageId());
            if(ObjectUtil.isNotNull(packageInfoDO)){
                if(ObjectUtils.isNotEmpty(packageInfoDO.getSupplierType()) && 2==packageInfoDO.getSupplierType()){
                    isOneSup=false;
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
                    BidConfirmQuotationDetailDO bidConfirmQuotationDetailDO=new BidConfirmQuotationDetailDO();
                    if(finalIsOneSup){
                        bidConfirmQuotationDetailDO  = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>()
                                        .eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                    }else {
                        SupplierInfoDO supplierInfoDO= supplierInfoMapper.selectOne(new LambdaQueryWrapperX<SupplierInfoDO>()
                                .eq(SupplierInfoDO::getSupplierId,contract.getSupplierId())
                                .eq(SupplierInfoDO::getPackageId,contract.getBuyPlanPackageId()));
                        bidConfirmQuotationDetailDO  = bidConfirmQuotationDetailMapper.selectOne(
                                new LambdaQueryWrapper<BidConfirmQuotationDetailDO>()
                                        .eq(BidConfirmQuotationDetailDO::getSupplierName,supplierInfoDO.getSupplierName())
                                        .eq(BidConfirmQuotationDetailDO::getPackageDetailId, detail.getDetailId()));
                    }
                    if(ObjectUtil.isNotNull(bidConfirmQuotationDetailDO)){

                        //规格参数/服务内容
                        if (ObjectUtil.isNotEmpty(bidConfirmQuotationDetailDO) && StringUtils.isNotEmpty(bidConfirmQuotationDetailDO.getModelSpecification())) {
                            contractBillVo.setSpec(bidConfirmQuotationDetailDO.getModelSpecification());
                        } else {
                            contractBillVo.setSpec("【无】");
                        }
                        //总价格
                        contractBillVo.setTotalPrice(bidConfirmQuotationDetailDO.getTotalPrice() != null ? Double.parseDouble(bidConfirmQuotationDetailDO.getTotalPrice()) : null);
                        //采购数量
                        contractBillVo.setPurchaseNum(bidConfirmQuotationDetailDO.getCount() != null ? bidConfirmQuotationDetailDO.getCount().doubleValue() : null);
                        contractBillVo.setPrice(bidConfirmQuotationDetailDO.getUnitPrice() != null ? bidConfirmQuotationDetailDO.getUnitPrice().doubleValue() : null);
                    }
                    return contractBillVo;
                })
                .collect(Collectors.toList());
    }
    /**
     * 处理合同支付计划
     *
     * @param contractVo 合同主体
     */
    public void contractPaymentPlanProcessor(ContractVo contractVo) {

//        List<PaymentScheduleDO> payments = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractVo.getContractGuid());
////        List<ContractPaymentPlanDO> payments = contractPaymentPlanMapper.selectList(ContractPaymentPlanDO::getContractId, contractVo.getContractGuid());
//        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(payments)) {
//            List<PaymentPlanDTO> paymentPlanVOS = ContractPaymentPlayConverter.INSTANCE.toVoList2(payments);
//            contractVo.setPaymentPlanList(paymentPlanVOS);
//        }

        //获取合同支付计划
        List<PaymentScheduleDO> contractPaymentPlanDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eqIfPresent(PaymentScheduleDO::getContractId, contractVo.getContractGuid()));
        List<PaymentPlanDTO> planDTOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(contractPaymentPlanDOList)) {
            planDTOS = ContractPaymentPlayConverter.INSTANCE.toVoList(contractPaymentPlanDOList);
            for (int i = 0; i < planDTOS.size(); i++) {
                PaymentPlanDTO paymentPlanDTO = planDTOS.get(i);
                //如果支付金额等于0，并且阶段支付金额不为空，就把阶段支付金额赋值给支付金额
                if (paymentPlanDTO.getMoney() == 0 && ObjectUtil.isNotEmpty(paymentPlanDTO.getStagePaymentAmount())) {
                    paymentPlanDTO.setMoney(paymentPlanDTO.getStagePaymentAmount());
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getMoney())) {
                    paymentPlanDTO.setMoney(contractVo.getTotalMoney());
                }
                if (ObjectUtil.isEmpty(paymentPlanDTO.getPayee())) {
                    paymentPlanDTO.setPayee(contractVo.getSupplierName());
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
        } else {
                //都没有草拟一条
                PaymentPlanDTO paymentPlanDTO = new PaymentPlanDTO().setContractId(contractVo.getContractGuid()).setPayee("1").setMoney(contractVo.getTotalMoney()).setPeriods(0).setPayTerm("").setPayDate(System.currentTimeMillis()).setPayProportion(0.0);
                planDTOS.add(paymentPlanDTO);
        }
        contractVo.setPaymentPlanList(planDTOS);
    }

    private Date setDate(LocalDateTime time) {
        if (ObjectUtil.isEmpty(time)) {
            return null;
        }
        Date date = null;
        try {
            String timeStr = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (StringUtils.isNotBlank(timeStr)) {
                System.out.println("中标时间---->" + timeStr);
                SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                date = SimpleDateFormat.parse(timeStr);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
    /**
     * 处理合同供应商
     *
     * @param contractVo 合同主体
     */
    public void contractSupplierProcessor(ContractVo contractVo, ContractOrderExtDO orderContractDO) {

        List<ContractSupplierVo> contractSupplierVos = new ArrayList<>();
            //设置供应商信息
            ContractSupplierVo contractSupplierVo = ContractSupplierConverter.INSTANCE.convert(orderContractDO);
        SupplyDTO supply = supplyApi.getSupply(orderContractDO.getSupplierId());
        if(ObjectUtil.isNull(supply)){
            supply = hljSupplyService.getSupply(orderContractDO.getSupplierId());
        }
            if (ObjectUtils.isNotEmpty(supply)) {
                setSupplierInfo( contractSupplierVo, supply, contractVo);
                contractSupplierVo.setPayee(1);
            }
            contractSupplierVos.add(contractSupplierVo);
            contractVo.setSupplierList(contractSupplierVos);
        }

    /**
     * 处理合同条目
     *
     * @param contractVo 合同主体
     */
    public void contractBillsProcessor(ContractVo contractVo, ContractOrderExtDO orderContractDO) {
        List<ContractGoodsDO> goods = contractGoodsMapper.selectList(ContractGoodsDO::getContractId, contractVo.getContractGuid());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goods)) {
            List<ContractBillVo> contractBillVos = ContractGoodsConverter.INSTANCE.toVoList(goods);
            if(CollectionUtil.isNotEmpty(contractBillVos)){
                contractBillVos.forEach(item->{
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
                    item.setContractBillStatisticalVo(statisticalVo);
                });
            }
            contractVo.setItemList(contractBillVos);
        } else {
            if (PlatformEnums.JDMALL.getCode().equals(orderContractDO.getPlatform())
                    || PlatformEnums.ZHUBAJIE.getCode().equals(orderContractDO.getPlatform())
                    || PlatformEnums.GPMALL.getCode().equals(orderContractDO.getPlatform())
                    || PlatformEnums.GP_GPFA.getCode().equals(orderContractDO.getPlatform())
            ) {
                String orderId = orderContractDO.getOrderId();
                List<GoodsDO> goodsDOS = new ArrayList<>();
                //合并订单信息
                List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId,orderContractDO.getId());
                if(CollectionUtil.isNotEmpty(orderRelDOS) && orderRelDOS.size()>1){
                    goodsDOS =  gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>()
                            .in(GoodsDO::getOrderId, orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList())));
                }else {
                    goodsDOS =  gpMallGoodsMapper.selectList(GoodsDO::getOrderId, orderId);
                }
                Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
                List<ContractBillVo> vos2 = ContractGoodsConverter.INSTANCE.toVOS2(goodsDOS);
                AssociatedPlanDO associatedPlanDO = associatedPlanMapper.selectOne(AssociatedPlanDO::getOrderId, orderContractDO.getOrderId());
                List<GoodsPurCatalogDO> goodsPurCatalogDOS = goodsIds.size()==0 ? null : goodsPurCatalogMapper.selectList(GoodsPurCatalogDO::getGoodsId, goodsIds);
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
                for (ContractBillVo contractBillVo : vos2) {
                    totalMoney = totalMoney + contractBillVo.getTotalPrice();
                }
//                contractVo.setTotalMoney(totalMoney);
                contractVo.setItemList(vos2);
            }
        }
    }

}
