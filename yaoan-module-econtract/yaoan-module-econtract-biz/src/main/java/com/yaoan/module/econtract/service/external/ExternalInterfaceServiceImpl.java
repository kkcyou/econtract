package com.yaoan.module.econtract.service.external;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.pojo.ExternallnterfaceResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.order.*;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.convert.gcy.gpmall.AssociatedPlanConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.filerecord.FileRecordDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.CancellationFileDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractDetailsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.ContractCancellationMapper;
import com.yaoan.module.econtract.dal.mysql.filerecord.FileRecordMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PackageInfoMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ClientModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractArchiveStateEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.OrderStatusEnums;
import com.yaoan.module.econtract.service.gpmall.GPMallContractService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.service.workday.WorkDayService;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 对外接口实现类
 *
 * @author zhc
 * @since 2024-04-24
 */
@Service
@Slf4j
public class ExternalInterfaceServiceImpl implements ExternalInterfaceService {
    @Resource
    private GPMallOrderMapper gpMallOrderMapper;
    @Resource
    private GPMallOrderAccessoryMapper gpMallOrderAccessoryMapper;
    @Resource
    private GPMallFileAttachmentMapper gpMallFileAttachmentMapper;
    @Resource
    private GPMallEngineeringProjectMapper gpMallEngineeringProjectMapper;

    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private GPMallProjectMapper gpMallProjectMapper;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;

    @Resource
    private SuperVisionApi superVisionApi;

    @Resource
    private GPMallContractService gPMallContractService;
    @Resource
    private FileApi fileApi;
    @Resource
    private ModelCategoryMapper modelCategoryMapper;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ClientModelCategoryMapper clientModelCategoryMapper;

    @Resource
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    @Resource
    private ContractDetailsMapper contractDetailsMapper;
    @Resource
    private GoodsPurCatalogMapper goodsPurCatalogMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private RegionApi regionApi;
    @Resource
    private WorkDayService workDayService;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private FileRecordMapper fileRecordMapper;
    @Resource
    private ContractApi contractApi;



    private static List<Date> parseDateString(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(dateString, Date[].class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse date string", e);
        }
    }

    public static void main(String[] args) {
        EncryptDTO encryptDTO = null;
        try {
//            String s = Sm4Utils.decryptEcb("fe0e4a350d8d6472e4d58b9063ae7f2a959bc019575b0bbd64d04564427124a8");
////            s = Sm4Utils.decryptEcb("6f48e628a2f58c16774031621a1dfde26ab5c9bfe39f09d98a75c89ed45d7358e2472ad0494546a334da8f852d476cd310fca14001f1c4d40e550bdd767d2ebd646379a3e33e7e64e849bb2180ee7d71925957a269c026ab0da380e75d267d38");
//            System.out.println(s);
//            String s1 = Sm4Utils.encryptEcb("{\n" + "    \"pageNo\": 1,\n" + "    \"pageSize\": 1,\n" + "    \"platform\":\"gp-gpfa\",\n" + "    \"categoryId\":\"1046\"\n" + "}");
//            System.out.println(s1);
//            String s2 = DigestUtils.sha256Hex("{\n" + "    \"pageNo\": 1,\n" + "    \"pageSize\": 1,\n" + "    \"platform\":\"gp-gpfa\",\n" + "    \"categoryId\":\"1046\"\n" + "}");
//            System.out.println(s2);

            String ss = "f7427d1a5f104da9f4898afd03eb20cf3fb7a516735ac406cebf4e6eefd5693182b840eeebb1af6030dca6648247470426fd9bb9d3b46cabec284c29b64a1f062e892aa3100b90c1cf234597edd04c1e";
            String string2 = Sm4Utils.decryptEcb(ss);
//            String string1 = Sm4Utils.decryptEcb(string);
            System.out.println(string2);

//            String s = "{" + "\"orderGuid\":\"DD24063010107020\"," + "\"platform\":\"JdMall\"," +"\"orderStatus\":\"2\""+ "}";
            String s = "{" + "\"contractGuid\":\"6e5ce6c957fc601d94db64c5dfff09f3\"," + "\"platform\":\"zhubajie\"" + "}";
            String string = Sm4Utils.encryptEcb(s);
            String string1 = DigestUtils.sha256Hex(s);
            System.out.println(string);
            System.out.println(string1);
        } catch (Exception e) {


        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public EncryptDTO saveOrderInfo(EncryptDTO encryptDTO) {
        DraftOrderInfoVO draftOrderInfoVO = null;
        try {
            draftOrderInfoVO = JSONObject.parseObject(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), DraftOrderInfoVO.class);

            List<DraftOrderInfo> draftOrderInfoList = draftOrderInfoVO.getDraftOrderInfoList();
            List<String> orderGuidList = draftOrderInfoList.stream().map(DraftOrderInfo::getOrderGuid).collect(Collectors.toList());
            Long count = gpMallOrderMapper.selectCount(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderGuidList));
            if (count > 0) {
                throw outException(ErrorCodeConstants.DATA_EXIST, "存在订单重复推送");
            }
            DraftOrderInfoVO finalDraftOrderInfoVO = draftOrderInfoVO;
            draftOrderInfoList.forEach(draftOrderInfo -> {
                draftOrderInfo.setContractFrom(finalDraftOrderInfoVO.getPlatform());
            });
            setInfos(draftOrderInfoList);
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, "订单接收失败：" + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EncryptDTO saveOrderInfo4Temp(EncryptDTO encryptDTO) {
        DraftOrderInfoVO draftOrderInfoVO = null;
        try {
            List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, encryptDTO.getMac()));
            List<DraftOrderInfo> draftOrderInfoList = GPMallOrderConverter.INSTANCE.do2VO4TempList(draftOrderInfoDOS);
            List<String> orderGuidList = draftOrderInfoList.stream().map(DraftOrderInfo::getOrderGuid).collect(Collectors.toList());
            Long count = gpMallOrderMapper.selectCount(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderGuidList));
            if (count > 0) {
                throw outException(ErrorCodeConstants.DATA_EXIST, "存在订单重复推送");
            }
            DraftOrderInfoVO finalDraftOrderInfoVO = draftOrderInfoVO;
            draftOrderInfoList.forEach(draftOrderInfo -> {
                draftOrderInfo.setContractFrom(finalDraftOrderInfoVO.getPlatform());
            });
            setInfos(draftOrderInfoList);
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, "订单接收失败 " + e.getMessage());
        }
    }

    @Override
    public void checkSignGetClass(EncryptDTO encryptDTO) {
        try {
            //验签 数据加密前的签名
            String mac = encryptDTO.getMac();
            //获取解密后的数据
            String formerDate = Sm4Utils.decryptEcb(encryptDTO.getRequestParam());
            String nowMac = DigestUtils.sha256Hex(formerDate);
            if (!nowMac.equals(mac)) {
                throw outException(ErrorCodeConstants.SIGN_ERROR2);
            }
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.SIGN_ERROR2);
        }
    }

    @Override
    public EncryptDTO saveGPMallOrderInfo(EncryptDTO encryptDTO) {
        DraftOrderInfoVO draftOrderInfoVO = null;
        try {
            draftOrderInfoVO = JSONObject.parseObject(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), DraftOrderInfoVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        setInfos(draftOrderInfoVO.getDraftOrderInfoList());
        try {
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EncryptDTO updateOrderStatus(EncryptDTO encryptDTO) {
        OrderReqDTO orderStatusVO = null;
        try {
            orderStatusVO = JSONObject.parseObject(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), OrderReqDTO.class);
            //查询起草合同
            List<ContractOrderExtDO> orderContractDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().
                    eq(ContractOrderExtDO::getOrderId, orderStatusVO.getOrderGuid())
                    .eq(ContractOrderExtDO::getPlatform, orderStatusVO.getPlatform())
                    .ne(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode())
                    .select(ContractOrderExtDO::getOrderId, ContractOrderExtDO::getId, ContractOrderExtDO::getStatus));
            List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()));
            if (draftOrderInfoDOS.isEmpty() && orderContractDOS.isEmpty()) {
                return Sm4Utils.convertParam("success");
            }
//            if (ObjectUtils.isNotEmpty(orderContractDOS)) {
            switch (OrderStatusEnums.getInstance(orderStatusVO.getOrderStatus())) {
                //发起取消
                case INITIATE_CANCEL:
                    updateInitiateCancelStatus(orderStatusVO, orderContractDOS);
                    break;
                //确认取消
                case CONFIRM_CANCEL:
                    updateConfirmCancelStatus(orderStatusVO, orderContractDOS);
                    break;
                //删除
                case DELETE:
                    updateDeleteStatus(orderStatusVO, orderContractDOS);
                    break;
                //作废
                case NULLIFY:
                    updateNullifyStatus(orderStatusVO, orderContractDOS);
                    break;
                //已完成
                case FINISH:
                    orderFinish(orderStatusVO, orderContractDOS);
                    break;
                //恢复正常
                case RESTORE_NORMAL:
                    contractRestoreNormal(orderStatusVO, orderContractDOS);
                    break;
                //退货
                case RETURNS:
                    break;
                default:
                    break;
            }

//            }
//        else {
//                switch (OrderStatusEnums.getInstance(orderStatusVO.getOrderStatus())) {
//                    //发起取消
//                    case INITIATE_CANCEL:
//                        break;
//                    //确认取消
//                    case CONFIRM_CANCEL:
//                        if (PlatformEnums.ZHUBAJIE.getCode().equals(orderStatusVO.getPlatform())) {
//                            gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getStatus, GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode()));
//                        } else {
//                            gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.CONFIRM_CANCEL.getInfo()));
//                            deleteOrderRelevancyInfo(Collections.singletonList(orderStatusVO.getOrderGuid()));
//                        }
//                        break;
//                    //删除
//                    case DELETE:
//                        gpMallOrderMapper.delete(new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()));
//                        deleteOrderRelevancyInfo(Collections.singletonList(orderStatusVO.getOrderGuid()));
//                        break;
//                    //作废
//                    case NULLIFY:
//                        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.NULLIFY.getCode()).set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.NULLIFY.getInfo()));
//                        deleteOrderRelevancyInfo(Collections.singletonList(orderStatusVO.getOrderGuid()));
//                        break;
//                    //已完成
//                    case FINISH:
//                        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode()).set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.FINISH.getInfo()));
//                        break;
//                    //恢复正常
//                    case RESTORE_NORMAL:
//                        break;
//                    //退货
//                    case RETURNS:
//                        break;
//                }
//            }
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, e.getMessage());
        }
    }

    //TODO
    @Override
    public EncryptDTO updateStatus(DraftOrderInfoDO draftOrderInfoDO) {
        try {
            if (ObjectUtils.isNotEmpty(draftOrderInfoDO)&& ObjectUtils.isNotEmpty(draftOrderInfoDO.getOrderGuid()) && ObjectUtils.isNotEmpty(draftOrderInfoDO.getStatus())) {
                gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, draftOrderInfoDO.getOrderGuid())
                        .set(DraftOrderInfoDO::getStatus, draftOrderInfoDO.getStatus()));

                if(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(draftOrderInfoDO.getStatus())){
                    List<ContractOrderExtDO> contractOrderExtDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().eq(ContractOrderExtDO::getOrderId, draftOrderInfoDO.getOrderGuid()));
                    if (CollectionUtil.isNotEmpty(contractOrderExtDOS)){
//                        contractOrderExtMapper.deleteBatchIds(contractOrderExtDOS.stream().map(ContractOrderExtDO::getId).filter(Objects::nonNull).collect(Collectors.toList()));
//                        contractMapper.deleteBatchIds(contractOrderExtDOS.stream().map(ContractOrderExtDO::getId).filter(Objects::nonNull).collect(Collectors.toList()));
                        List<ContractDO> contractDOList = new ArrayList<>();
                        contractOrderExtDOS.stream().forEach(
                                item -> {
                                    item.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());

                                    ContractDO contractDO = new ContractDO();
                                    contractDO.setId(item.getId());
                                    contractDO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
                                    contractDOList.add(contractDO);
                                }
                        );
                        contractOrderExtMapper.updateBatch(contractOrderExtDOS);
                        contractMapper.updateBatch(contractDOList);

                    }
                }
            }
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, e.getMessage());
        }
    }
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Override
    public EncryptDTO updateStatusPackage(PackageInfoDO packageInfoDO) {
        try {
            if (ObjectUtils.isNotEmpty(packageInfoDO)&& ObjectUtils.isNotEmpty(packageInfoDO.getPackageGuid()) && ObjectUtils.isNotEmpty(packageInfoDO.getHidden())) {
                packageInfoMapper.updateById(packageInfoDO);
                //恢复包信息并将合同变成已作废
                if( packageInfoDO.getHidden() == 0){
                    List<TradingContractExtDO> tradingContractExtDOS = tradingContractExtMapper.selectList(
                            new LambdaQueryWrapperX<TradingContractExtDO>().eq(TradingContractExtDO::getBuyPlanPackageId, packageInfoDO.getPackageGuid()));
                    if (CollectionUtil.isNotEmpty(tradingContractExtDOS)) {
//                        tradingContractExtMapper.deleteBatchIds(tradingContractExtDOS.stream().map(TradingContractExtDO::getId).filter(Objects::nonNull).collect(Collectors.toList()));
//                        contractMapper.deleteBatchIds(tradingContractExtDOS.stream().map(TradingContractExtDO::getId).filter(Objects::nonNull).collect(Collectors.toList()));
                        List<ContractDO> contractDOList =new ArrayList<>();
                        tradingContractExtDOS.stream().forEach(
                                item ->{
                                    item.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());

                                    ContractDO contractDO =new ContractDO();
                                    contractDO.setId(item.getId());
                                    contractDO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
                                    contractDOList.add(contractDO);
                                }
                        );
                        tradingContractExtMapper.updateBatch(tradingContractExtDOS);
                        contractMapper.updateBatch(contractDOList);
                    }
                }
            }
            return Sm4Utils.convertParam("success");
        } catch (Exception e) {
            throw outException(ErrorCodeConstants.DATA_DEAL_ERROR, e.getMessage());
        }
    }

    private void orderFinish(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds.add(orderStatusVO.getOrderGuid());
        if (CollectionUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, ids);
            if (CollectionUtil.isNotEmpty(orderRelDOS)) {
                orderIds.addAll(orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
            }
        }
        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIds)
                .eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode())
                .set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.FINISH.getInfo()));
    }

    private void contractRestoreNormal(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        //合同恢复正常
        if (ObjectUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                    .in(ContractOrderExtDO::getId, ids).eq(ContractOrderExtDO::getPlatform, orderStatusVO.getPlatform())
                    .ne(ContractOrderExtDO::getStatus, -2)
                    .set(ContractOrderExtDO::getModify, 0));
            contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>()
                    .in(ContractDO::getId, ids).eq(ContractDO::getPlatform, orderStatusVO.getPlatform())
                    .ne(ContractDO::getStatus, -2)
                    .set(ContractDO::getIsLocked, 0));
        }
    }

    @Override
    public String getContractSignInfo(ContractOrderExtDO encryptDTO) throws Exception{
        try{
            ContractOrderExtDO orderExtDO = contractOrderExtMapper.selectOne(ContractOrderExtDO::getId, encryptDTO.getId());
            Integer extStatus = null;
            Integer contractStatus = null;//orderExtDO.getStatus();
            if (encryptDTO.getStatus() == null || encryptDTO.getStatus() == 0) {
                extStatus = HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode();
                contractStatus = ContractStatusEnums.SIGN_COMPLETED.getCode();
            } else {
                //供应商已盖章待采购人盖章
                extStatus = HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELIVERED.getCode();
                //判断是否走用印审批
                if (contractApi.isNeedSignet(null,encryptDTO.getId())) {
                    contractStatus = ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode();
                } else {
                    contractStatus = ContractStatusEnums.TO_BE_SIGNED.getCode();
                }
            }
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                    .eq(ContractOrderExtDO::getId, orderExtDO.getId())
                    .set(ContractOrderExtDO::getPdfFileId, encryptDTO.getPdfFileId())
                    .set(ContractOrderExtDO::getPdfFilePath, encryptDTO.getPdfFilePath())
                    .set(ContractOrderExtDO::getDocument, 0) //设置未归档
                    .set(ContractOrderExtDO::getContractSignTime, encryptDTO.getContractSignTime())
                    .set(ContractOrderExtDO::getOrgSinTime, encryptDTO.getOrgSinTime())
                    .set(ContractOrderExtDO::getSupSinTime, encryptDTO.getSupSinTime())
                    .set(ContractOrderExtDO::getStatus, extStatus));
            Date signDate =  encryptDTO.getContractSignTime();
            if(signDate == null ){
                signDate = encryptDTO.getOrgSinTime() == null?new Date():encryptDTO.getOrgSinTime();
            }
            
            if (encryptDTO.getPdfFileId() != null && encryptDTO.getPdfFileId() != 0) {
                contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, orderExtDO.getId())
                        .set(ContractDO::getStatus, contractStatus)
                        .set(ContractDO::getSignDate,signDate)
                        .set(ContractDO::getIsSign,0)
                        .set(ContractDO::getPdfFileId, encryptDTO.getPdfFileId()));
                
            } else {
                //供应商确认后-采购人签章时同步没有pdf_file_id
                contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, orderExtDO.getId())
                        .set(ContractDO::getStatus, contractStatus).set(ContractDO::getSignDate,signDate).set(ContractDO::getIsSign,0)
                );
            }
            fileRecordMapper.delete(FileRecordDO::getContractId, orderExtDO.getId());
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        

        return "success";
    }

    @Resource
    private ContractCancellationMapper contractCancellationMapper;

    @Override
    public String getContractBack(BpmContractCreateReqVO encryptDTO) {
        ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectOne(ContractOrderExtDO::getId, encryptDTO.getId());
        if (ObjectUtil.isEmpty(contractOrderExtDO)) {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同不存在");
        }
        contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>()
                .eq(ContractDO::getId, contractOrderExtDO.getId())
                .set(ContractDO::getIsSign,0)
                .set(ContractDO::getStatus, ContractStatusEnums.TO_BE_SIGNED.getCode()));
        fileRecordMapper.delete(FileRecordDO::getContractId, contractOrderExtDO.getId());
        //设置为采购人签署
        contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                .eq(ContractOrderExtDO::getId, contractOrderExtDO.getId())
                .set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELIVERED.getCode()));
        //插入退回记录表
        contractCancellationMapper.insert(new CancellationFileDO().setContractId(encryptDTO.getId()).setReason(encryptDTO.getReason()));
        return "success";
    }

    private void updateInitiateCancelStatus(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        //把合同改为不可修改
        if (ObjectUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                    .in(ContractOrderExtDO::getId, ids).eq(ContractOrderExtDO::getPlatform, orderStatusVO.getPlatform())
                    .ne(ContractOrderExtDO::getStatus, -2)
                    .set(ContractOrderExtDO::getModify, 1));
            contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>()
                    .in(ContractDO::getId, ids).eq(ContractDO::getPlatform, orderStatusVO.getPlatform())
                    .ne(ContractDO::getStatus, -2)
                    .set(ContractDO::getIsLocked, 1));
        }
    }

    private void updateConfirmCancelStatus(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds.add(orderStatusVO.getOrderGuid());
        if (CollectionUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            //删除合同
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>().eq(ContractOrderExtDO::getOrderId, orderStatusVO.getOrderGuid())
                    .in(ContractOrderExtDO::getId, ids)
                    .ne(ContractOrderExtDO::getStatus, -2)
                    .set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()));
            contractMapper.deleteBatchIds(ids);
            List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, ids);
            if (CollectionUtil.isNotEmpty(orderRelDOS)) {
                orderIds.addAll(orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
            }
        }

//        deleteContract(orderStatusVO);
        //修改数据统计表关联合同状态为已删除
        statisticsMapper.delete(new LambdaUpdateWrapper<StatisticsDO>().in(StatisticsDO::getOrgId, orderIds));
//        deletePushBackupDate(orderContractDOS);
        //判断是否是合并订单取消所有订单
        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>()
                .in(DraftOrderInfoDO::getOrderGuid, orderIds).eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform())
                .set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.CONFIRM_CANCEL.getInfo())
                .set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()));
    }

//    private void deletePushBackupDate(List<ContractOrderExtDO> orderContractDOS) {
//        List<String> contractIds = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(contractIds)) {
//            pushBackupDataMapper.delete(new LambdaUpdateWrapper<PushBackupDataDO>().in(PushBackupDataDO::getContractId, contractIds));
//        }
//    }

    private void updateStatus(OrderReqDTO orderStatusVO) {
        //合同改为已取消
        contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>().eq(ContractOrderExtDO::getOrderId, orderStatusVO.getOrderGuid())
                .eq(ContractOrderExtDO::getPlatform, orderStatusVO.getPlatform()).set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()));
        //修改订单状态改为已取消
        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid())
                .eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode())
                .set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.CONFIRM_CANCEL.getInfo()));
        deleteOrderRelevancyInfo(Collections.singletonList(orderStatusVO.getOrderGuid()));
        //修改数据统计表订单为已取消,合同已取消
        statisticsMapper.update(null, new LambdaUpdateWrapper<StatisticsDO>().eq(StatisticsDO::getOrgId, orderStatusVO.getOrderGuid()).set(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).set(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()));
    }

    private void updateDeleteStatus(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        //合同状态改为删除
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds.add(orderStatusVO.getOrderGuid());
        if (CollectionUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            //删除合同
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>().eq(ContractOrderExtDO::getOrderId, orderStatusVO.getOrderGuid())
                    .in(ContractOrderExtDO::getId, ids)
                    .ne(ContractOrderExtDO::getStatus, -2)
                    .set(ContractOrderExtDO::getStatus, -1));
            contractMapper.deleteBatchIds(ids);
            List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, ids);
            if (CollectionUtil.isNotEmpty(orderRelDOS)) {
                orderIds.addAll(orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
            }
        }
//        deletePushBackupDate(orderContractDOS) ;
        //订单状态改为已删除
//            gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderStatusVO.getOrderGuid()).eq(DraftOrderInfoDO::getPlatform, orderStatusVO.getPlatform()).set(DraftOrderInfoDO::getStatus, OrderStatusEnums.DELETE.getCode()).set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.DELETE.getInfo()));
        gpMallOrderMapper.delete(new LambdaUpdateWrapper<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIds)
                .eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform()));
        //删除数据统计表订单
        statisticsMapper.delete(new LambdaUpdateWrapper<StatisticsDO>().in(StatisticsDO::getOrderId, orderIds));
        //删除订单关联信息
        deleteOrderRelevancyInfo(orderIds);
    }

    private void updateNullifyStatus(OrderReqDTO orderStatusVO, List<ContractOrderExtDO> orderContractDOS) {
        //合同状态改为作废
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds.add(orderStatusVO.getOrderGuid());
        if (CollectionUtil.isNotEmpty(orderContractDOS)) {
            List<String> ids = orderContractDOS.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
            //删除合同
            contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>().eq(ContractOrderExtDO::getOrderId, orderStatusVO.getOrderGuid())
                    .in(ContractOrderExtDO::getId, ids)
                    .ne(ContractOrderExtDO::getStatus, -2)
                    .set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
            contractMapper.deleteBatchIds(ids);
            List<ContractOrderRelDO> orderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, ids);
            if (CollectionUtil.isNotEmpty(orderRelDOS)) {
                orderIds.addAll(orderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
            }
        }
//        deletePushBackupDate(orderContractDOS);
        //修改订单为已作废
        gpMallOrderMapper.update(null, new LambdaUpdateWrapper<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIds)
                .eq(DraftOrderInfoDO::getContractFrom, orderStatusVO.getPlatform())
                .set(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.NULLIFY.getCode())
                .set(DraftOrderInfoDO::getOrderStatusStr, OrderStatusEnums.NULLIFY.getInfo()));
        //删除订单关联信息
        deleteOrderRelevancyInfo(orderIds);
        //数据统计表订单为已作废
        statisticsMapper.delete(new LambdaUpdateWrapper<StatisticsDO>().in(StatisticsDO::getOrderId, orderIds));
    }

    private void deleteOrderRelevancyInfo(List<String> orderIds) {
        List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(GoodsDO::getOrderId, orderIds);
        Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(goodsIds)) {
            //删除商品关联品目信息
            goodsPurCatalogMapper.delete(new LambdaQueryWrapperX<GoodsPurCatalogDO>().inIfPresent(GoodsPurCatalogDO::getGoodsId, goodsIds));
        }
        if (CollectionUtil.isNotEmpty(orderIds)) {
            //删除此订单关联的配件信息
            gpMallOrderAccessoryMapper.delete(new LambdaQueryWrapperX<OrderAccessoryDO>().inIfPresent(OrderAccessoryDO::getOrderId, orderIds));
            //删除此订单关联的附件信息
            gpMallFileAttachmentMapper.delete(new LambdaQueryWrapperX<FileAttachmentDO>().inIfPresent(FileAttachmentDO::getOrderId, orderIds));
            //删除此订单关联的商品信息
            gpMallGoodsMapper.delete(new LambdaQueryWrapperX<GoodsDO>().inIfPresent(GoodsDO::getOrderId, orderIds));
            //删除此订单关联的项目信息
            gpMallProjectMapper.delete(new LambdaQueryWrapperX<ProjectDO>().inIfPresent(ProjectDO::getOrderId, orderIds));
            //删除此订单关联的工程项目信息
            gpMallEngineeringProjectMapper.delete(new LambdaQueryWrapperX<EngineeringProjectDO>().inIfPresent(EngineeringProjectDO::getOrderId, orderIds));
            //删除关联计划信息
            associatedPlanMapper.delete(new LambdaQueryWrapperX<AssociatedPlanDO>().inIfPresent(AssociatedPlanDO::getOrderId, orderIds));
            //删除该订单绑定的合同基本信息
            contractInfoBackupsMapper.delete(new LambdaQueryWrapperX<ContractInfoBackupsDO>().inIfPresent(ContractInfoBackupsDO::getOrderId, orderIds));

        }

    }

    private void setInfos(List<DraftOrderInfo> draftOrderInfoList) {
        //数据预处理
        List<DraftOrderInfoDO> finalDraftOrderInfoDOList = new ArrayList<>();
        List<OrderAccessoryDO> finalOrderAccessoryDOList = new ArrayList<>();
        List<FileAttachmentDO> finalFileAttachmentDOList = new ArrayList<>();
        List<GoodsDO> finalGoodsDOList = new ArrayList<>();
        List<EngineeringProjectDO> finalEngineeringProjectDOList = new ArrayList<>();
        List<ProjectDO> finalProjectDOList = new ArrayList<>();
        List<AssociatedPlanDO> associatedPlanDOList = new ArrayList<>();
        List<ContractInfoBackupsDO> finalContractInfoBackupsDOList = new ArrayList<>();
        List<GoodsPurCatalogDO> goodsPurCatalogDOS = new ArrayList<>();
        List<StatisticsDO> statisticsDOList = new ArrayList<>();
        //整合数据
        for (DraftOrderInfo info : draftOrderInfoList) {
            DraftOrderInfoDO draftOrderInfoDO = GPMallOrderConverter.INSTANCE.convertDTO2DO(info);
            //订单合并 计划信息 赋值到外层
            if (ObjectUtil.isNotEmpty(info.getAssociatedPlanVO())) {
                draftOrderInfoDO.setBuyPlanName(info.getAssociatedPlanVO().getBuyPlanName());
                draftOrderInfoDO.setBuyPlanCode(info.getAssociatedPlanVO().getBuyPlanCode());
                draftOrderInfoDO.setBuyPlanAmount(info.getAssociatedPlanVO().getBuyPlanMoney());
            }
            String orderGuId = draftOrderInfoDO.getOrderGuid();
            if (ObjectUtil.isEmpty(draftOrderInfoDO.getRegionCode())) {
                //获取采购人id
                String orgId = info.getPurchaserOrgGuid();
                //根据采购人id查询区划
                OrganizationDTO organization = organizationApi.getOrganization(orgId);
                if (ObjectUtil.isNotEmpty(organization)) {
                    draftOrderInfoDO.setRegionCode(organization.getRegionCode() != null ? organization.getRegionCode() : null);
                }
            }
            finalDraftOrderInfoDOList.add(draftOrderInfoDO);
            //配件信息
            if (CollectionUtil.isNotEmpty(info.getAllAccessoryList())) {
                List<OrderAccessoryDO> orderAccessoryDOList = GPMallOrderConverter.INSTANCE.accessoryVOList2DOList(info.getAllAccessoryList());
                List<OrderAccessoryDO> updatedList = orderAccessoryDOList.stream().map(order -> {
                    order.setOrderId(orderGuId);
                    return order;
                }).collect(Collectors.toList());
                finalOrderAccessoryDOList.addAll(updatedList);
            }
            //附件信息
            if (CollectionUtil.isNotEmpty(info.getFileAttachmentVOList())) {
                List<FileAttachmentDO> fileAttachmentDOList = GPMallOrderConverter.INSTANCE.fileAttachmentVOList2DOList(info.getFileAttachmentVOList());
                List<FileAttachmentDO> updatedList = fileAttachmentDOList.stream().map(order -> {
                    order.setOrderId(orderGuId);
                    return order;
                }).collect(Collectors.toList());
                finalFileAttachmentDOList.addAll(updatedList);
            }
            //货物信息
            if (CollectionUtil.isNotEmpty(info.getGoodsVOList())) {
                List<GoodsVO> goodsVOList = info.getGoodsVOList();
                goodsVOList.stream().forEach(item -> {
                    if (CollectionUtil.isNotEmpty(item.getPurCatalogCodeList())) {
                        List<PurCatalogInfoVo> purCatalogCodeList = item.getPurCatalogCodeList();
                        if (CollectionUtil.isEmpty(purCatalogCodeList)) {
                            throw outException(ErrorCodeConstants.DATA_MISSING, "采购目录编码不能为空");
                        }
                        for (PurCatalogInfoVo purCatalogInfoVo : purCatalogCodeList) {
                            GoodsPurCatalogDO goodsPurCatalogDO = new GoodsPurCatalogDO().setPurCatalogCode(purCatalogInfoVo.getPurCatalogCode()).setPurCatalogName(purCatalogInfoVo.getPurCatalogName()).setGoodsGuid(item.getGoodsGuid());
                            goodsPurCatalogDOS.add(goodsPurCatalogDO);
                        }
                    }
                });
                List<GoodsDO> goodsDOList = GPMallOrderConverter.INSTANCE.goodsVOList2DOList(info.getGoodsVOList());
                List<GoodsDO> updatedList = goodsDOList.stream().map(goods -> {
                    goods.setOrderId(orderGuId);
                    return goods;
                }).collect(Collectors.toList());
                finalGoodsDOList.addAll(updatedList);
                //如果没传采购分类（项目分类编码）取商品里的
                if (StringUtils.isBlank(info.getProjectCategoryCode())) {
                    String purCatalogType = goodsVOList.get(0).getPurCatalogType();
                    if (StringUtils.isBlank(purCatalogType)) {
                        throw outException(ErrorCodeConstants.DATA_MISSING, "采购分类不能为空");
                    }
                    switch (purCatalogType) {
                        case "1":
                            info.setProjectCategoryCode("A");
                            break;
                        case "2":
                            info.setProjectCategoryCode("B");
                            break;
                        case "3":
                            info.setProjectCategoryCode("C");
                            break;
                    }
                }
            }
            //工程信息
            if (ObjectUtil.isNotNull(info.getEngineeringProjectVO())) {
                EngineeringProjectDO engineeringProjectDO = GPMallOrderConverter.INSTANCE.engineeringProjectVO2DO(info.getEngineeringProjectVO());
                engineeringProjectDO.setOrderId(orderGuId);
                finalEngineeringProjectDOList.add(engineeringProjectDO);
            }
            //项目信息
            if (ObjectUtil.isNotNull((info.getProjectVO()))) {
            //校验项目名称不可为空
            if (StringUtils.isBlank(info.getProjectVO().getProjectName())) {
                throw outException(ErrorCodeConstants.DATA_MISSING, "项目名称不可为空");
            }
            //校验项目编号不可为空
            if (StringUtils.isBlank(info.getProjectVO().getProjectCode())) {
                throw outException(ErrorCodeConstants.DATA_MISSING, "项目编号不可为空");
            }
            ProjectDO projectDO = GPMallOrderConverter.INSTANCE.projectVO2DO(info.getProjectVO());
            projectDO.setOrderId(orderGuId);
            finalProjectDOList.add(projectDO);
            }
            //关联计划信息
            if (ObjectUtil.isNotNull(info.getAssociatedPlanVO())) {
                //校验计划编号（备案号）不可为空
                if (StringUtils.isBlank(info.getAssociatedPlanVO().getBuyPlanCode())) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "采购计划编号不可为空");
                }
                if (StringUtils.isBlank(info.getAssociatedPlanVO().getBuyPlanName())) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "采购计划名称不可为空");
                }
                if (StringUtils.isBlank(info.getAssociatedPlanVO().getBuyPlanId())) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "采购计划id不可为空");
                }
                AssociatedPlanDO associatedPlanDO = AssociatedPlanConverter.INSTANCE.toAssociatedPlanDO(info.getAssociatedPlanVO());
                associatedPlanDO.setOrderId(orderGuId);
                associatedPlanDOList.add(associatedPlanDO);
            }
            try {
                statisticsDOList = GPMallOrderConverter.INSTANCE.toStatisticsList(finalDraftOrderInfoDOList);
                //将计划编号填入集合中
                if (ObjectUtil.isNotEmpty(statisticsDOList)) {
                    Map<String, String> planMap = associatedPlanDOList.stream().collect(Collectors.toMap(AssociatedPlanDO::getOrderId, AssociatedPlanDO::getBuyPlanCode));
                    statisticsDOList.forEach(item -> {
                        String buyPlanCode = planMap.get(item.getOrderId());
                        if (buyPlanCode != null) {
                            item.setBuyPlanCode(buyPlanCode);
                        }
                        //计算超期时间
                        String contractSignEndTime = null;
                        try {
                            contractSignEndTime = workDayService.getContractSignEndTime(1, 1, 6, item.getWinBidTime().toLocalDate());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (contractSignEndTime != null) {
                            item.setOverdueTime(LocalDateTime.of(LocalDate.parse(contractSignEndTime), LocalTime.MAX));
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            //合同数据备份
            ContractInfoBackupsDO contractInfoBackupsDO = new ContractInfoBackupsDO();
            contractInfoBackupsDO.setOrderId(orderGuId);
            contractInfoBackupsDO.setContractInfo(JsonUtils.toJsonString(info.getContractDataVo()));
            finalContractInfoBackupsDOList.add(contractInfoBackupsDO);
        }
        //获取订单id
        Set<String> orderIds = draftOrderInfoList.stream().map(DraftOrderInfo::getOrderGuid).collect(Collectors.toSet());
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(DraftOrderInfoDO::getOrderGuid, orderIds);
        if (CollectionUtil.isNotEmpty(draftOrderInfoDOS)) {
            //修改
            Map<String, DraftOrderInfoDO> orderInfoDOMap = CollectionUtils.convertMap(draftOrderInfoDOS, DraftOrderInfoDO::getOrderGuid);
            List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(GoodsDO::getOrderId, orderIds);
            Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
            //删除商品关联品目信息
            if (CollectionUtil.isNotEmpty(goodsIds)) {
                goodsPurCatalogMapper.delete(new LambdaQueryWrapperX<GoodsPurCatalogDO>().inIfPresent(GoodsPurCatalogDO::getGoodsId, goodsIds));
            }
            if (CollectionUtil.isNotEmpty(orderIds)) {
                //删除此订单关联的配件信息
                gpMallOrderAccessoryMapper.delete(new LambdaQueryWrapperX<OrderAccessoryDO>().inIfPresent(OrderAccessoryDO::getOrderId, orderIds));
                //删除此订单关联的附件信息
                gpMallFileAttachmentMapper.delete(new LambdaQueryWrapperX<FileAttachmentDO>().inIfPresent(FileAttachmentDO::getOrderId, orderIds));
                //删除此订单关联的商品信息
                gpMallGoodsMapper.delete(new LambdaQueryWrapperX<GoodsDO>().inIfPresent(GoodsDO::getOrderId, orderIds));
                //删除此订单关联的项目信息
                gpMallProjectMapper.delete(new LambdaQueryWrapperX<ProjectDO>().inIfPresent(ProjectDO::getOrderId, orderIds));
                //删除此订单关联的工程项目信息
                gpMallEngineeringProjectMapper.delete(new LambdaQueryWrapperX<EngineeringProjectDO>().inIfPresent(EngineeringProjectDO::getOrderId, orderIds));
                //删除关联计划信息
                associatedPlanMapper.delete(new LambdaQueryWrapperX<AssociatedPlanDO>().inIfPresent(AssociatedPlanDO::getOrderId, orderIds));
            }
            finalDraftOrderInfoDOList.forEach(item -> {
                item.setId(orderInfoDOMap.get(item.getOrderGuid()).getId());
            });
            statisticsDOList.forEach(item -> {
                item.setId(orderInfoDOMap.get(item.getOrderId()).getId());
            });
            //更新订单信息
            gpMallOrderMapper.updateBatch(finalDraftOrderInfoDOList);
            //更新数据统计表
            statisticsMapper.updateBatch(statisticsDOList);
            //删除该订单绑定的合同基本信息
            if (CollectionUtil.isNotEmpty(finalContractInfoBackupsDOList) && CollectionUtil.isNotEmpty(orderIds)) {
                contractInfoBackupsMapper.delete(new LambdaQueryWrapperX<ContractInfoBackupsDO>().inIfPresent(ContractInfoBackupsDO::getOrderId, orderIds));
            }
        } else {
            //新增
            checkOrderGuid(draftOrderInfoList);
            //新增订单信息
            gpMallOrderMapper.insertBatch(finalDraftOrderInfoDOList);
            //将订单插入数据统计表
            statisticsMapper.insertBatch(statisticsDOList);
        }
        //批量插入订单的配件信息
        gpMallOrderAccessoryMapper.insertBatch(finalOrderAccessoryDOList);
        //批量插入订单的附件信息
        gpMallFileAttachmentMapper.insertBatch(finalFileAttachmentDOList);
        //批量插入订单的商品信息
        gpMallGoodsMapper.insertBatch(finalGoodsDOList);
        //批量插入订单的项目信息
        gpMallProjectMapper.insertBatch(finalProjectDOList);
        //批量插入订单的工程项目信息
        gpMallEngineeringProjectMapper.insertBatch(finalEngineeringProjectDOList);
        //批量插入关联计划信息
        associatedPlanMapper.insertBatch(associatedPlanDOList);
        //将数据进行备份
        if (CollectionUtil.isNotEmpty(finalContractInfoBackupsDOList)) {
            contractInfoBackupsMapper.insertBatch(finalContractInfoBackupsDOList);
        }
        Map<String, GoodsDO> goodsDOMap = CollectionUtils.convertMap(finalGoodsDOList, GoodsDO::getGoodsGuid);
        goodsPurCatalogDOS.forEach(item -> {
            item.setGoodsId(goodsDOMap.get(item.getGoodsGuid()).getId());
        });
        //新增商品关联品目信息
        goodsPurCatalogMapper.insertBatch(goodsPurCatalogDOS);
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
     * 校验数据库里是否有存在订单id
     */
    void checkOrderGuid(List<DraftOrderInfo> draftOrderInfoList) {
        List<String> orderGuidList = draftOrderInfoList.stream().map(DraftOrderInfo::getOrderGuid).collect(Collectors.toList());
        Long count = gpMallOrderMapper.selectCount(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderGuidList));
        if (count > 0) {
            throw outException(ErrorCodeConstants.DATA_EXIST, "存在订单重复推送");
        }
    }


}
