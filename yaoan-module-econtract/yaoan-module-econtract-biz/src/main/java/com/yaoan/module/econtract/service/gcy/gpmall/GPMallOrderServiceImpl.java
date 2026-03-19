package com.yaoan.module.econtract.service.gcy.gpmall;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.api.gcy.order.PurCatalogInfoVo;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.BigGPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GPMallOrderDetailReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GroupedDraftOrderInfoVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.OrderIdListVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GoodsRespVO;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.OrderStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.enums.supervise.PurCatalogTypeEnums;
import com.yaoan.module.econtract.service.catalog.PurCatalogService;
import com.yaoan.module.econtract.service.gpmall.GPMallContractService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.util.AmountUtil;
import com.yaoan.module.econtract.util.ContractCodeUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.service.user.SupplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IF_ORDER_MERGE;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 12:01
 */
@Slf4j
@Service
public class GPMallOrderServiceImpl implements GPMallOrderService {
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
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
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractParamFieldMapper contractParamFieldMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RegionApi regionApi;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;
    @Resource
    private GoodsPurCatalogMapper goodsPurCatalogMapper;
    @Resource
    private PurCatalogService purCatalogService;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private GPMallContractService gpMallContractService;
    @Resource
    private SupplyApi supplyApi;

    @Override
    public void saveOrderInfo(DraftOrderInfo info) {
        DraftOrderInfoDO draftOrderInfoDO = new DraftOrderInfoDO();
        draftOrderInfoDO = GPMallOrderConverter.INSTANCE.convertDTO2DO(info);
        //配件信息
        if (CollectionUtil.isNotEmpty(info.getAllAccessoryList())) {
            List<OrderAccessoryDO> orderAccessoryDOList = GPMallOrderConverter.INSTANCE.accessoryVOList2DOList(info.getAllAccessoryList());
            gpMallOrderAccessoryMapper.insertBatch(orderAccessoryDOList);
        }
        //附件信息
        if (CollectionUtil.isNotEmpty(info.getFileAttachmentVOList())) {
            List<FileAttachmentDO> fileAttachmentDOList = GPMallOrderConverter.INSTANCE.fileAttachmentVOList2DOList(info.getFileAttachmentVOList());
            gpMallFileAttachmentMapper.insertBatch(fileAttachmentDOList);
        }
        //货物信息
        if (CollectionUtil.isNotEmpty(info.getGoodsVOList())) {
            List<GoodsDO> goodsDOList = GPMallOrderConverter.INSTANCE.goodsVOList2DOList(info.getGoodsVOList());
            gpMallGoodsMapper.insertBatch(goodsDOList);
        }
        //工程信息
        if (ObjectUtil.isNotNull(info.getEngineeringProjectVO())) {
            EngineeringProjectDO engineeringProjectDO = GPMallOrderConverter.INSTANCE.engineeringProjectVO2DO(info.getEngineeringProjectVO());
            gpMallEngineeringProjectMapper.insert(engineeringProjectDO);
        }
        //项目信息
        if (ObjectUtil.isNotNull(info.getProjectVO())) {
            ProjectDO projectDO = GPMallOrderConverter.INSTANCE.projectVO2DO(info.getProjectVO());
            gpMallProjectMapper.insert(projectDO);
        }

        gpMallOrderMapper.insert(draftOrderInfoDO);

    }

    @Override
    public ContractDataDTO queryByOrgOrderV2(OrderIdListVO orderIdListVO) {
//        ContractDTO contractDTO = new ContractDTO();
//        String token = ecmsGcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
//        //校验此订单是否存在
//        DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderId).select(
//                DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getContractFrom));
//        if (ObjectUtil.isEmpty(draftOrderInfoDO)) {
//            //为空说明订单不存在，卖场，曜安数据不同步，需查询此订单信息进行订单更新
//            RestResponseDTO<DraftOrderInfo> restResponseDTO = null;
//            if (PlatformEnums.GP_GPFA.getCode().equals(contractFrom)) {
//                //框彩平台-调用框彩平台的订单信息查询接口
//                restResponseDTO = gpfaOpenApi.getOrderInfo(token, orderId);
//            } else if (PlatformEnums.GPMALL.getCode().equals(contractFrom)) {
//                //电子卖场-调用电子卖场的订单信息查询接口
//                restResponseDTO = openApi.getOrderInfo(token, orderId);
//            }
//
//            DraftOrderInfo draftOrderInfo = restResponseDTO.getData();
//            Boolean success = restResponseDTO.getSuccess();
//            if (ObjectUtils.isNotEmpty(draftOrderInfo)) {
//                List<DraftOrderInfo> draftOrderInfoList = new ArrayList<>();
//                draftOrderInfoList.add(draftOrderInfo);
//                //保存订单信息
//                gPMallOrderService.saveOrderInfo(draftOrderInfoList);
//                //重新获取订单信息
//                draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderId).select(
//                        DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getContractFrom));
//
//            } else if (!success) {
//                throw exception(ErrorCodeConstants.GOMall_Query_Error, restResponseDTO.getMessage());
//            }
//        }
//        RestResponseDTO<ContractDataDTO> contractContentData = null;
//        //订单存在或更新订单后根据订单id调用api接口获取模板填充数据
//        if (PlatformEnums.GP_GPFA.getCode().equals(contractFrom)) {
//            //框彩平台-调用框彩平台的获取起草合同时需要的业务数据接口
//            contractContentData = gpfaOpenApi.getContractContentData(token, orderId);
//        } else if (PlatformEnums.GPMALL.getCode().equals(contractFrom)) {
//            //电子卖场-调用电子卖场的获取起草合同时需要的业务数据接口
//            contractContentData = openApi.getContractContentData(token, orderId);
//        }
//        if (ObjectUtil.isEmpty(contractContentData.getData()) && !contractContentData.getSuccess()) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, contractContentData.getMessage());
//        }
//        ContractDataDTO data = contractContentData.getData();
//        //将合同草拟数据进行备份
//        if (ObjectUtil.isNotEmpty(data)) {
//            Long aLong = contractInfoBackupsMapper.selectCount(ContractInfoBackupsDO::getOrderId, orderId);
//            if (aLong > 0) {
//                contractInfoBackupsMapper.updateById(new ContractInfoBackupsDO().setOrderId(orderId).setContractInfo(JsonUtils.toJsonString(data)));
//            } else {
//                contractInfoBackupsMapper.insert(new ContractInfoBackupsDO().setOrderId(orderId).setContractInfo(JsonUtils.toJsonString(data)));
//            }
//            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//            data.setUserType(loginUser.getType());
//            //获取计划信息
//            //采购计划编号
//            Map<String, Object> resultMap = data.getResultMap();
//            try {
//                if (ObjectUtil.isNotEmpty(resultMap)) {
//                    String buyPlanCode = (String) resultMap.get("buyPlanCode");
//                    if (ObjectUtil.isEmpty(buyPlanCode)) {
//                        AssociatedPlanDO associatedPlanDO = associatedPlanMapper.selectOne(new LambdaQueryWrapperX<AssociatedPlanDO>()
//                                .eq(AssociatedPlanDO::getBuyPlanCode, buyPlanCode));
//                        if (ObjectUtil.isNotEmpty(associatedPlanDO) || ObjectUtil.isNotEmpty(associatedPlanDO.getBuyPlanCode())) {
//                            resultMap.put("buyPlanCode", associatedPlanDO.getBuyPlanCode());
//                        }
//                    }
//                    data.setResultMap(resultMap);
//                }
//            } catch (Exception e) {
//                log.error("获取计划编号错误！");
//            }
//        }
//        //获取项目信息
//        ProjectDO projectDO = gpMallProjectMapper.selectOne(ProjectDO::getOrderId, orderId);
//        String type = ProjectCategoryEnums.GOODS.getCode();
//        if (ObjectUtil.isNotEmpty(projectDO)) {
//            type = projectDO.getProjectCategoryCode();
//            contractDTO = data.getContractDTO();
//            contractDTO.setProjectCode(projectDO.getProjectCode());
//            contractDTO.setProjectName(projectDO.getProjectName());
//        } else if (ObjectUtil.isNotEmpty(data)) {
//            contractDTO = data.getContractDTO();
//        }
//        List<ModelIdVO> modelIdList = purCatalogService.getModelIdByOrderCode(draftOrderInfoDO == null ? null : draftOrderInfoDO.getId(), null, type, null, null, null);
//        //查询模板顺序配置开关 模板顺序0通用类模板在前 1平台通用类在前
//        if (CollectionUtil.isNotEmpty(modelIdList)) {
//            data.setModelId(modelIdList.get(0).getModelId());
//        }
//        data.setOrderCode(draftOrderInfoDO.getOrderCode());
//        //设置合同分类
//        contractDTO.setProjectCategoryCode(type);
//        data.setContractDTO(contractDTO);
//        return data;
        return null;
    }

    @Override
    public ContractDataDTO queryByOrgOrderV3(OrderIdListVO orderIdListVO) {
        //设置相对方id和采购人id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<ContractOrderExtDO> orderContractDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().in(ContractOrderExtDO::getOrderId, orderIdListVO.getIdList()).notIn(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode()).select(ContractOrderExtDO::getId, ContractOrderExtDO::getStatus, ContractOrderExtDO::getContractDrafter, ContractOrderExtDO::getOrderId));
        orderContractDOS.forEach(item -> {
            if (item.getContractDrafter().equals(loginUser.getType())) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "此订单已起草合同，请到合同管理查看");
            }
            if (!item.getStatus().equals(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode())) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "该订单已存在在途合同，请到合同管理查看");
            }
        });

        ContractDataDTO contractDataVo = new ContractDataDTO();
        ContractDTO contractDTO = new ContractDTO();
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIdListVO.getIdList()).eqIfPresent(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode()));
        if (CollectionUtil.isEmpty(draftOrderInfoDOS)) {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "查询订单" + orderIdListVO.getIdList() + "不存在");
        }
        // 京东卖场 业务目前没有插入 合同备份表数据， 之后其他场景可能需要完善  --- 单位端 2024-11-06
//        ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(ContractInfoBackupsDO::getOrderId, orderId);
//        if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
//            String contractInfo = contractInfoBackupsDO.getContractInfo();
//            contractDataVo = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
//            contractDTO = contractDataVo.getContractDTO();
//        }
        //设置合同编号
        contractDTO.setContractCode(ContractCodeUtil.getContractCode(orderIdListVO.getContractFrom()));
        DraftOrderInfoDO draftOrderInfoDO = draftOrderInfoDOS.size() == 0 ? null : draftOrderInfoDOS.get(0);
        //设置成交百分比
        contractDTO.setTransactionRatio(draftOrderInfoDO == null ? null : draftOrderInfoDO.getTransactionRatio());
        //合同总金额-所有订单相加
        if (CollectionUtil.isNotEmpty(draftOrderInfoDOS)) {
            BigDecimal reduce = draftOrderInfoDOS.stream().map(DraftOrderInfoDO::getOrderTotalAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            contractDTO.setTotalMoney(reduce == null ? null : reduce);
            contractDTO.setShiftMoney(reduce == null ? null : AmountUtil.trsferCapital(reduce.doubleValue()));
        }
        //设置采购单位id,名称
        if (StringUtils.isEmpty(contractDTO.getBuyerOrgGuid())) {
            //为空取订单中的采购单位id
            contractDTO.setBuyerOrgGuid(draftOrderInfoDO == null ? null : draftOrderInfoDO.getPurchaserOrgGuid());

        }
        //设置供应商id,名称
        if (StringUtils.isEmpty(contractDTO.getSupplierGuid())) {
            //为空取订单中的供应商id
            contractDTO.setSupplierGuid(draftOrderInfoDO == null ? null : draftOrderInfoDO.getSupplierGuid());
        }
        //设置采购人信息
        OrganizationDTO organization = organizationApi.getOrganization(contractDTO.getBuyerOrgGuid());
        if (ObjectUtils.isNotEmpty(organization)) {
            contractDTO.setBuyerOrgName(organization.getName());
            contractDTO.setBuyerProxy(organization.getLinkMan());
            contractDTO.setBuyerLinkMobile(organization.getLinkPhone());
            contractDTO.setOrglinkFax(organization.getLinkFax());
            contractDTO.setDeliveryAddress(organization.getAddress());
            RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionByGuid(organization.getRegionGuid());
            contractDTO.setRegionCode(organization.getRegionCode());
            if (StringUtils.isEmpty(organization.getRegionCode())) {
                contractDTO.setRegionCode(region == null ? null : region.getRegionCode());
            }
            contractDTO.setRegionName(region == null ? null : region.getRegionName());
            if (ObjectUtil.isNull(contractDTO.getPayType())) {
                contractDTO.setPayType(organization.getPayType());
            }
        }
        //设置合同来源
        contractDTO.setContractFrom(draftOrderInfoDO == null ? null : draftOrderInfoDO.getContractFrom());
        //设置供应商信息
        SupplyDTO supply = supplyApi.getSupply(contractDTO.getSupplierGuid());
        if(ObjectUtil.isNull(supply)){
            supply = hljSupplyService.getSupply(contractDTO.getSupplierGuid());
        }
        if (ObjectUtils.isNotEmpty(supply)) {
            contractDTO.setSupplierProxy(supply.getLegalPerson());
            contractDTO.setRegisteredAddress(supply.getLegalAddr());
            contractDTO.setSupplierLinkMobile(supply.getPersonMobile());
            contractDTO.setBankName(supply.getBankName());
            contractDTO.setBankAccount(supply.getBankAccount());
            contractDTO.setSupplierLocation(supply.getRegAddr());
            contractDTO.setSupplierName(supply.getSupplyCn());
        }
        //设置采购标项Guid
        String purCatalogCode = null;
        BigDecimal totalMoney = new BigDecimal(0);
        List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapper<GoodsDO>().in(GoodsDO::getOrderId, orderIdListVO.getIdList()));
        if (CollectionUtil.isNotEmpty(goodsDOS)) {
            List<GoodsVO> goodsVOS = GPMallOrderConverter.INSTANCE.toGoodsVOS(goodsDOS);
            contractDTO.setBuyPlanBillGuid(goodsDOS.size() == 0 ? null : goodsDOS.get(0).getBuyPlanBillGuid());
            Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
            List<GoodsPurCatalogDO> goodsPurCatalogDOS = goodsIds == null ? null : goodsPurCatalogMapper.selectList(GoodsPurCatalogDO::getGoodsId, goodsIds);
            Map<String, GoodsPurCatalogDO> goodsPurCatalogDOMap = CollectionUtils.convertMap(goodsPurCatalogDOS, GoodsPurCatalogDO::getGoodsId);
            goodsVOS.forEach(goodsVO -> {
                String goodsId = goodsVO.getId();
                GoodsPurCatalogDO goodsPurCatalogDO = goodsPurCatalogDOMap == null ? null : goodsPurCatalogDOMap.get(goodsId);
                PurCatalogInfoVo purCatalogInfoVo = new PurCatalogInfoVo();
                purCatalogInfoVo.setPurCatalogName(goodsPurCatalogDO == null ? null : goodsPurCatalogDO.getPurCatalogName());
                purCatalogInfoVo.setPurCatalogCode(goodsPurCatalogDO == null ? null : goodsPurCatalogDO.getPurCatalogCode());
                List<PurCatalogInfoVo> purCatalogCodeList = new ArrayList<>();
                purCatalogCodeList.add(purCatalogInfoVo);
                goodsVO.setPurCatalogCodeList(purCatalogCodeList);
            });
            contractDataVo.setGoodsVOS(goodsVOS);
            if (CollectionUtil.isNotEmpty(goodsPurCatalogDOS)) {
                purCatalogCode = goodsPurCatalogDOS.get(0).getPurCatalogCode();
            }
            for (GoodsDO goodsDO : goodsDOS) {
                totalMoney = totalMoney.add(goodsDO.getTotalMoney());
            }
            contractDTO.setTotalMoney(totalMoney);
            contractDTO.setShiftMoney(AmountUtil.trsferCapital(totalMoney.doubleValue()));
        }
        List<ModelIdVO> modelIdList = purCatalogService.getModelIdByOrderCode(draftOrderInfoDO == null ? null : draftOrderInfoDO.getId(), purCatalogCode, null, null, null, null);
        //查询模板顺序配置开关  模板顺序0通用类模板在前  1平台通用类在前
        if (CollectionUtil.isNotEmpty(modelIdList)) {
            contractDataVo.setModelId(modelIdList.get(0).getModelId());
        }
//            contractDataVo.setModelId(OrderContractConverter.INSTANCE.modelIdVOToDTO(modelIdList));
//            }

        //设置是否涉密采购
        List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(AssociatedPlanDO::getOrderId, orderIdListVO.getIdList());
        AssociatedPlanDO associatedPlanDO = new AssociatedPlanDO();
        if (CollectionUtil.isNotEmpty(associatedPlanDOS)) {
            associatedPlanDO = associatedPlanDOS.get(0);
        }
        contractDTO.setSecret(associatedPlanDO == null ? null : associatedPlanDO.getSecret());
        //采购计划备案书/核准书编号
        contractDTO.setBuyPlanCode(associatedPlanDO == null ? null : associatedPlanDO.getBuyPlanCode());
        //设置采购方式
        contractDTO.setPurchaseMethod(associatedPlanDO == null ? null : associatedPlanDO.getPurchaseMethod());
//        contractDTO.setKind(associatedPlanDO == null ? null :associatedPlanDO.getKind());
        //设置项目所属分类==采购分类
        contractDTO.setPurCatalogType(goodsDOS.size() == 0 ? null : goodsDOS.get(0).getPurCatalogType());
        contractDataVo.setUserType(loginUser.getType());
        contractDTO.setDatenow(new Date());
        //设置项目名称，项目ID，项目编号，
        List<ProjectDO> projectDOS = gpMallProjectMapper.selectList(ProjectDO::getOrderId, orderIdListVO.getIdList());
        ProjectDO projectDO = new ProjectDO();
        if (CollectionUtil.isNotEmpty(projectDOS)) {
            projectDO = projectDOS.get(0);
        }
        if (ObjectUtil.isNotNull(projectDO)) {
            contractDTO.setProjectName(projectDO.getProjectName());
            if (PlatformEnums.ZHUBAJIE.getCode().equals(orderIdListVO.getContractFrom())) {
                contractDTO.setProjectGuid(projectDO.getProjectCode());
                contractDTO.setProjectCode(projectDO.getProjectGuid());
            } else {
                contractDTO.setProjectGuid(projectDO.getProjectGuid());
                contractDTO.setProjectCode(projectDO.getProjectCode());
            }
            if (StringUtils.isEmpty(contractDTO.getKind())) {
                contractDTO.setKind(projectDO.getKind());
            }
        }
        if (ObjectUtil.isNotNull(draftOrderInfoDO) && ObjectUtil.isNotNull(draftOrderInfoDO.getProjectCategoryCode())) {
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(draftOrderInfoDO.getProjectCategoryCode());
            switch (projectCategoryEnums) {
                case GOODS:
                    contractDTO.setContractName("政府采购货物买卖合同（试行）");
                    break;
                case ENGINEER:
                    contractDTO.setContractName("政府采购工程类采购合同(试行)");
                    break;
                case SERVICE:
                    contractDTO.setContractName("政府采购服务类采购合同（试行）");
                    break;
            }
        }
        if (ObjectUtil.isNull(contractDTO.getContractName())) {
            PurCatalogTypeEnums purCatalogTypeEnums = PurCatalogTypeEnums.getInstance(contractDTO.getPurCatalogType());
            switch (purCatalogTypeEnums) {
                case GOODS:
                    contractDTO.setContractName("政府采购货物买卖合同（试行）");
                    break;
                case ENGINEER:
                    contractDTO.setContractName("政府采购工程类采购合同(试行)");
                    break;
                case SERVICE:
                    contractDTO.setContractName("政府采购服务类采购合同（试行）");
                    break;
            }
        }
        //设置合同分类
        if (ObjectUtil.isNull(draftOrderInfoDO.getProjectCategoryCode())) {
            contractDTO.setProjectCategoryCode(contractDTO.getPurCatalogType() == null ? null : PurCatalogTypeEnums.getInstance(contractDTO.getPurCatalogType()).getCode());
        } else {
            contractDTO.setProjectCategoryCode(draftOrderInfoDO.getProjectCategoryCode());
        }
        contractDataVo.setContractDTO(contractDTO);
//        ContractDataDTO contractDataDTO = OrderContractConverter.INSTANCE.toContractDataDTO(contractDataVo);
        return contractDataVo;
    }

    @Override
    public PageResult<GroupedDraftOrderInfoVO> listGPMallOrgOrder(GPMallPageReqVO vo) {
        //合同起草方：采购人（1）/供应商（2）。默认为供应商
        Integer contractDrafter = null;
        if (PlatformEnums.GP_GPFA.getCode().equals(vo.getContractFrom()) || PlatformEnums.GPMALL.getCode().equals(vo.getContractFrom())) {
            //定点协议，框彩
            contractDrafter = SecurityFrameworkUtils.getLoginUser().getType();
        }

        if (0 == vo.getFlag() && !vo.getContractFrom().equals(PlatformEnums.GPMS_GPX.getCode())) {
            List<ContractOrderRelDO> contractDOList = contractOrderRelMapper.listGPMallOrgOrder(vo);
            if (ObjectUtil.isNotEmpty(contractDOList)) {
                List<String> orderIdList = contractDOList.stream().map(ContractOrderRelDO::getOrderId).filter(Objects::nonNull).collect(Collectors.toList());
                vo.setOrderIdList(orderIdList);
            }
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.selectOrgPage(vo, contractDrafter);
        if (CollectionUtil.isEmpty(draftOrderInfoDOPageResult.getList())) {
            return PageResult.empty();
        }
        //填充合并订单后的子订单数据9999
        List<DraftOrderInfoDO> list = draftOrderInfoDOPageResult.getList();
        List<String> buyPlanCodeList = list.stream().filter(Objects::nonNull).distinct().map(DraftOrderInfoDO::getBuyPlanCode).collect(Collectors.toList());
        List<String> supplierNameList = list.stream().filter(Objects::nonNull).distinct().map(DraftOrderInfoDO::getSupplierName).collect(Collectors.toList());
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>()
                .eq(DraftOrderInfoDO::getStatus, 0)
                .in(DraftOrderInfoDO::getSupplierName, supplierNameList).in(DraftOrderInfoDO::getBuyPlanCode, buyPlanCodeList));
        List<GPMallPageRespVO> gpMallPageRespVOS = GPMallOrderConverter.INSTANCE.listOrderDO2Resp(draftOrderInfoDOS);
        // 使用 Map 存储 orderCode 和 supplierName 的组合作为 key，对应的 GPMallPageRespVO 列表作为 value
        Map<String, List<GPMallPageRespVO>> orderCodeSupplierNameMap = new HashMap<>();
        for (GPMallPageRespVO gpMallPageRespVO : gpMallPageRespVOS) {
            String key = gpMallPageRespVO.getBuyPlanCode() + "_" + gpMallPageRespVO.getSupplierName();
            orderCodeSupplierNameMap.computeIfAbsent(key, k -> new ArrayList<>()).add(gpMallPageRespVO);
        }
        // 结果集
        PageResult<GroupedDraftOrderInfoVO> pageResult = GPMallOrderConverter.INSTANCE.convertOrderOrgRespPage(draftOrderInfoDOPageResult);

        for (GroupedDraftOrderInfoVO groupedDraftOrderInfoVO : pageResult.getList()) {
            // 根据 orderCode 和 supplierName 生成 key
            String key = groupedDraftOrderInfoVO.getBuyPlanCode() + "_" + groupedDraftOrderInfoVO.getSupplierName();
            // 直接从 Map 中获取数据
            List<GPMallPageRespVO> matchedGPMallPageRespVOS = orderCodeSupplierNameMap.get(key);
            if (CollectionUtil.isNotEmpty(matchedGPMallPageRespVOS)) {
                // 清空
                groupedDraftOrderInfoVO.getList().clear();
                // 填充到 groupedDraftOrderInfoVO 的 list 字段中
                groupedDraftOrderInfoVO.getList().addAll(matchedGPMallPageRespVOS);
                groupedDraftOrderInfoVO.setCount(Long.valueOf(matchedGPMallPageRespVOS.size()));
                groupedDraftOrderInfoVO.setBuyPlanName(groupedDraftOrderInfoVO.getList().get(0).getBuyPlanName());
                groupedDraftOrderInfoVO.setPurchaserOrg(groupedDraftOrderInfoVO.getList().get(0).getPurchaserOrg());
            }
        }
        List<GPMallPageRespVO> results = pageResult.getList().stream()
                .flatMap(group -> group.getList().stream())
                .collect(Collectors.toList());
        ;
        //订单状态修改，兼容之前逻辑
        enhanceList(results);
        return pageResult;
    }

    private void enhanceList(List<GPMallPageRespVO> respVOList) {
        Set<String> orderIds = respVOList.stream().map(GPMallPageRespVO::getOrderGuid).collect(Collectors.toSet());
        Set<String> orgIds = respVOList.stream().map(GPMallPageRespVO::getPurchaserOrgGuid).collect(Collectors.toSet());
        List<String> list = new ArrayList<>(orgIds);
        //获取项目信息
        List<ProjectDO> projectDOList = gpMallProjectMapper.selectList(new LambdaQueryWrapperX<ProjectDO>().inIfPresent(ProjectDO::getOrderId, orderIds).select(ProjectDO::getProjectCategoryCode, ProjectDO::getOrderId));
        Map<String, ProjectDO> projectDOMap = CollectionUtils.convertMap(projectDOList, ProjectDO::getOrderId);
        //获取商品信息
        List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().inIfPresent(GoodsDO::getOrderId, orderIds));
        Map<String, List<GoodsDO>> goodsMap = CollectionUtils.convertMultiMap(goodsDOS, GoodsDO::getOrderId);
        //获取采购计划信息
        List<AssociatedPlanDO> associatedPlanDOList = associatedPlanMapper.selectList(new LambdaQueryWrapperX<AssociatedPlanDO>().inIfPresent(AssociatedPlanDO::getOrderId, orderIds));
        Map<String, AssociatedPlanDO> associatedPlanDOMap = CollectionUtils.convertMap(associatedPlanDOList, AssociatedPlanDO::getOrderId);
        for (GPMallPageRespVO respVO : respVOList) {
            //设置商品信息和采购分类
            if (ObjectUtil.isNotEmpty(goodsMap)) {
                List<GoodsDO> goodsDOS1 = goodsMap.get(respVO.getOrderGuid());
                List<GoodsRespVO> goodsRespVO = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOS1);
                respVO.setGoodsRespVOList(goodsRespVO);
                respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());
                respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());
            }
            if (StringUtils.isEmpty(respVO.getOrderTypeStr())) {
                if (PlatformEnums.GP_GPFA.getCode().equals(respVO.getContractFrom()) || PlatformEnums.GPMALL.getCode().equals(respVO.getContractFrom())) {
                    //框彩和协议定点找项目信息
                    //若采购分类为空去项目表
                    if (CollectionUtil.isNotEmpty(projectDOMap)) {
                        ProjectDO projectDO = projectDOMap.get(respVO.getOrderGuid());
                        if (ObjectUtil.isNotNull(projectDO)) {
                            ProjectCategoryEnums projectCategoryEnums = projectDO.getProjectCategoryCode() == null ? null : ProjectCategoryEnums.getInstance(projectDO.getProjectCategoryCode());
                            if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                                respVO.setOrderTypeStr(projectCategoryEnums.getInfo());
                            }
                        }
                    }
                } else {
                    AssociatedPlanDO associatedPlanDO = associatedPlanDOMap.get(respVO.getOrderGuid());
                    if (ObjectUtil.isNotNull(associatedPlanDO)) {
                        //如果没传采购目录编码（项目分类编码）取商品里的
                        if (StringUtils.isBlank(associatedPlanDO.getPurchaseMethod())) {
                            String purCatalogType = associatedPlanDO.getPurchaseMethod();
                            switch (purCatalogType) {
                                case "1":
                                    respVO.setOrderTypeStr("A");
                                    break;
                                case "2":
                                    respVO.setOrderTypeStr("B");
                                    break;
                                case "3":
                                    respVO.setOrderTypeStr("C");
                                    break;
                            }
                        }
                        respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());
                    }

                }

            }
            //订单状态
            if (PlatformEnums.JDMALL.getCode().equals(respVO.getContractFrom()) || PlatformEnums.ZHUBAJIE.getCode().equals(respVO.getContractFrom())) {
                OrderStatusEnums orderStatusEnums = OrderStatusEnums.getInstance(respVO.getOrderStatus());
                if (ObjectUtil.isNotNull(orderStatusEnums)) {
                    respVO.setOrderStatusStr(orderStatusEnums.getInfo());
                }
            }
            //设置采购计划名称，采购计划编码，采购计划金额
            respVO.setBuyPlanName(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanName());
            respVO.setBuyPlanCode(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanCode());
            respVO.setBuyPlanAmount(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanMoney());
            //设置区划
            if (!PlatformEnums.GPMALL.getCode().equals(respVO.getContractFrom()) && !PlatformEnums.GP_GPFA.getCode().equals(respVO.getContractFrom())) {
                List<OrganizationDTO> organizationDTOS = organizationApi.getOrganizationByIds(list);
                Map<String, OrganizationDTO> buyerMap = CollectionUtils.convertMap(organizationDTOS, OrganizationDTO::getId);
                OrganizationDTO organizationDTO = buyerMap.get(respVO.getPurchaserOrgGuid());
                if (ObjectUtil.isNotEmpty(organizationDTO)) {
                    //获取区划信息
                    Set<String> regionIds = organizationDTOS.stream().map(OrganizationDTO::getRegionGuid).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
                    List<String> regionCodeList = new ArrayList<>(regionIds);
                    List<RegionDTO> regionByCode = new ArrayList<>();
                    if (CollectionUtil.isEmpty(regionCodeList)) {
                        regionByCode = null;
                    } else {
                        regionByCode = regionApi.getRegionByGuidS(regionCodeList);
                    }
                    Map<String, RegionDTO> regionCodeMap = CollectionUtils.convertMap(regionByCode, RegionDTO::getId);
                    respVO.setRegionFullName(regionCodeMap.get(organizationDTO.getRegionGuid()) == null ? null : regionCodeMap.get(organizationDTO.getRegionGuid()).getRegionName());
                    respVO.setRegionGuid(regionCodeMap.get(organizationDTO.getRegionGuid()) == null ? null : organizationDTO.getRegionGuid());
                }
            }


        }
    }

    @Override
    public List<GPMallPageRespVO> queryGPMallOrderOrgDetail(GPMallOrderDetailReqVO vo) {
        List<DraftOrderInfoDO> list = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>()
                .eq(DraftOrderInfoDO::getBuyPlanCode, vo.getOrderCode()).eq(DraftOrderInfoDO::getSupplierName, vo.getSupplierName()));
        PageResult<GPMallPageRespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPage(new PageResult<DraftOrderInfoDO>(list, Long.valueOf(list.size())));
        enhancePage(respVOPageResult);
        List<String> idS = list.stream().map(DraftOrderInfoDO::getOrderGuid).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(idS)) {
            return new ArrayList<>();
        }
        OrderIdListVO orderIdListVO = new OrderIdListVO();
        ContractDataDTO jdMall = queryByOrgOrderV3(orderIdListVO.setIdList(idS).setContractFrom("JdMall"));
        List<GPMallPageRespVO> resultList = respVOPageResult.getList();
        // 根据订单id填充标的信息
        Map<String, List<GoodsVO>> goodsVOMap = jdMall.getGoodsVOS().stream()
                .collect(Collectors.groupingBy(GoodsVO::getOrderGoodsGuid));
        for (GPMallPageRespVO gpMallPageRespVO : resultList) {
            String orderGuid = gpMallPageRespVO.getOrderGuid();
            if (ObjectUtil.isNotEmpty(jdMall.getGoodsVOS())) {
                // 根据orderGuid找到对应的GoodsVO列表
                List<GoodsVO> goodsVOList = goodsVOMap.get(orderGuid);
                if (CollectionUtil.isNotEmpty(goodsVOList)) {
                    // 将GoodsVO列表添加到GPMallPageRespVO的goodsVOS列表中
                    if (CollectionUtil.isEmpty(gpMallPageRespVO.getGoodsVOS())) {
                        gpMallPageRespVO.setGoodsVOS(new ArrayList<>());
                    }
                    gpMallPageRespVO.getGoodsVOS().addAll(goodsVOList);
                }
            }
        }
        return resultList;
    }

    @Override
    public PageResult<GPMallPageRespVO> listGPMallOrder(GPMallPageReqVO vo) {
        //合同起草方：采购人（1）/供应商（2）。默认为供应商
        Integer contractDrafter = null;
//        if (PlatformEnums.GP_GPFA.getCode().equals(vo.getContractFrom()) || PlatformEnums.GPMALL.getCode().equals(vo.getContractFrom())) {
//            //定点协议，框彩
//            contractDrafter = SecurityFrameworkUtils.getLoginUser().getType();
//        }
        if (0 == vo.getFlag() && !PlatformEnums.GPMS_GPX.getCode().equals(vo.getContractFrom())) {
            List<ContractOrderExtDO> contractDOList = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>()
                    .select(ContractOrderExtDO::getOrderId)
                    .eq(ContractOrderExtDO::getPlatform, vo.getContractFrom())
                    .notIn(ContractOrderExtDO::getStatus, CollectionUtil.newArrayList(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(),
                            HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode()))
                    .groupBy(ContractOrderExtDO::getOrderId));
            if (ObjectUtil.isNotEmpty(contractDOList)) {
                List<String> orderIdList = contractDOList.stream().filter(Objects::nonNull).map(ContractOrderExtDO::getOrderId).collect(Collectors.toList());
                vo.setOrderIdList(orderIdList);
            }
        }
        //只能看到本采购单位的订单
        LoginUser user= SecurityFrameworkUtils.getLoginUser();
        vo.setOrgId(user.getOrgId());
        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.selectPage(vo, contractDrafter);

        PageResult<GPMallPageRespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPage(draftOrderInfoDOPageResult);
        enhancePage(respVOPageResult);
        return respVOPageResult;
    }


    private void enhancePage(PageResult<GPMallPageRespVO> respVOPageResult) {
        if (CollectionUtil.isNotEmpty(respVOPageResult.getList())) {
            List<GPMallPageRespVO> respVOList = respVOPageResult.getList();
            Set<String> orderIds = respVOList.stream().map(GPMallPageRespVO::getOrderGuid).collect(Collectors.toSet());
            Set<String> orgIds = respVOList.stream().map(GPMallPageRespVO::getPurchaserOrgGuid).collect(Collectors.toSet());
            List<String> list = new ArrayList<>(orgIds);
            //获取项目信息
            List<ProjectDO> projectDOList = gpMallProjectMapper.selectList(new LambdaQueryWrapperX<ProjectDO>().inIfPresent(ProjectDO::getOrderId, orderIds).select(ProjectDO::getProjectCategoryCode, ProjectDO::getOrderId));
            Map<String, ProjectDO> projectDOMap = CollectionUtils.convertMap(projectDOList, ProjectDO::getOrderId);
            //获取商品信息
            List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().inIfPresent(GoodsDO::getOrderId, orderIds));
            Map<String, List<GoodsDO>> goodsMap = CollectionUtils.convertMultiMap(goodsDOS, GoodsDO::getOrderId);
            //获取采购计划信息
            List<AssociatedPlanDO> associatedPlanDOList = associatedPlanMapper.selectList(new LambdaQueryWrapperX<AssociatedPlanDO>().inIfPresent(AssociatedPlanDO::getOrderId, orderIds));
            Map<String, AssociatedPlanDO> associatedPlanDOMap = CollectionUtils.convertMap(associatedPlanDOList, AssociatedPlanDO::getOrderId);
            for (GPMallPageRespVO respVO : respVOList) {
                //设置商品信息和采购分类
                if (ObjectUtil.isNotEmpty(goodsMap)) {
                    List<GoodsDO> goodsDOS1 = goodsMap.get(respVO.getOrderGuid());
                    List<GoodsRespVO> goodsRespVO = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOS1);
                    respVO.setGoodsRespVOList(goodsRespVO);
                    respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());
                    respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());
                }
                if (StringUtils.isEmpty(respVO.getOrderTypeStr())) {
                    if (PlatformEnums.GP_GPFA.getCode().equals(respVO.getContractFrom()) || PlatformEnums.GPMALL.getCode().equals(respVO.getContractFrom())) {
                        //框彩和协议定点找项目信息
                        //若采购分类为空去项目表
                        if (CollectionUtil.isNotEmpty(projectDOMap)) {
                            ProjectDO projectDO = projectDOMap.get(respVO.getOrderGuid());
                            if (ObjectUtil.isNotNull(projectDO)) {
                                ProjectCategoryEnums projectCategoryEnums = projectDO.getProjectCategoryCode() == null ? null : ProjectCategoryEnums.getInstance(projectDO.getProjectCategoryCode());
                                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                                    respVO.setOrderTypeStr(projectCategoryEnums.getInfo());
                                }
                            }
                        }
                    } else {
                        AssociatedPlanDO associatedPlanDO = associatedPlanDOMap.get(respVO.getOrderGuid());
                        if (ObjectUtil.isNotNull(associatedPlanDO)) {
                            //如果没传采购目录编码（项目分类编码）取商品里的
                            if (StringUtils.isBlank(associatedPlanDO.getPurchaseMethod())) {
                                String purCatalogType = associatedPlanDO.getPurchaseMethod();
                                switch (purCatalogType) {
                                    case "1":
                                        respVO.setOrderTypeStr("A");
                                        break;
                                    case "2":
                                        respVO.setOrderTypeStr("B");
                                        break;
                                    case "3":
                                        respVO.setOrderTypeStr("C");
                                        break;
                                }
                            }
                            respVO.setOrderTypeStr(ProjectCategoryEnums.getInstance(respVO.getOrderType()) == null ? null : ProjectCategoryEnums.getInstance(respVO.getOrderType()).getInfo());

                        }

                    }

                }
                //订单状态
                if (PlatformEnums.JDMALL.getCode().equals(respVO.getContractFrom()) || PlatformEnums.ZHUBAJIE.getCode().equals(respVO.getContractFrom())) {
                    OrderStatusEnums orderStatusEnums = OrderStatusEnums.getInstance(respVO.getOrderStatus());
                    if (ObjectUtil.isNotNull(orderStatusEnums)) {
                        respVO.setOrderStatusStr(orderStatusEnums.getInfo());
                    }
                }
                //设置采购计划名称，采购计划编码，采购计划金额
                respVO.setBuyPlanName(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanName());
                respVO.setBuyPlanCode(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanCode());
                respVO.setBuyPlanAmount(associatedPlanDOMap.get(respVO.getOrderGuid()) == null ? null : associatedPlanDOMap.get(respVO.getOrderGuid()).getBuyPlanMoney());
                //设置区划
                if (!PlatformEnums.GPMALL.getCode().equals(respVO.getContractFrom()) && !PlatformEnums.GP_GPFA.getCode().equals(respVO.getContractFrom())) {
                    List<OrganizationDTO> organizationDTOS = organizationApi.getOrganizationByIds(list);
                    Map<String, OrganizationDTO> buyerMap = CollectionUtils.convertMap(organizationDTOS, OrganizationDTO::getId);
                    OrganizationDTO organizationDTO = buyerMap.get(respVO.getPurchaserOrgGuid());
                    if (ObjectUtil.isNotEmpty(organizationDTO)) {
                        //获取区划信息
                        Set<String> regionIds = organizationDTOS.stream().map(OrganizationDTO::getRegionGuid).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
                        List<String> regionCodeList = new ArrayList<>(regionIds);
                        List<RegionDTO> regionByCode = new ArrayList<>();
                        if (CollectionUtil.isEmpty(regionCodeList)) {
                            regionByCode = null;
                        } else {
                            regionByCode = regionApi.getRegionByGuidS(regionCodeList);
                        }
                        Map<String, RegionDTO> regionCodeMap = CollectionUtils.convertMap(regionByCode, RegionDTO::getId);
                        respVO.setRegionFullName(regionCodeMap.get(organizationDTO.getRegionGuid()) == null ? null : regionCodeMap.get(organizationDTO.getRegionGuid()).getRegionName());
                        respVO.setRegionGuid(regionCodeMap.get(organizationDTO.getRegionGuid()) == null ? null : organizationDTO.getRegionGuid());
                    }
                }

                //TODO 订单起草状态 因为起草合同订单列表筛选出的不是待起草的就是供应商起草的，所以直接判断赋值，，，如果合同列表查询逻辑变更此处逻辑需注意修改
                respVO.setNowStatusStr("0".equals(respVO.getStatus())?"未起草":"供应商起草中");
            }
        }
    }

    @Override
    public BigGPMallPageRespVO listGPMallOrders(GPMallPageReqVO vo) {

        //查询配置结果是否合并订单
        String value = systemConfigApi.getConfigByKey(IF_ORDER_MERGE.getKey());
        if (StringUtils.isBlank(value)) {
            log.error("该用户没有完成订单合并配置，请找管理员配置");
            throw exception(DIY_ERROR, "请找管理员配置后再试。");
        }
        BigGPMallPageRespVO result = new BigGPMallPageRespVO();
        result.setIfMergeOrder(value);
        IfEnums ifEnums = IfEnums.getInstance(value);
        switch (Objects.requireNonNull(ifEnums, "订单合并配置错误")) {
            //订单合并的电子卖场分页
            case YES:
                PageResult<GroupedDraftOrderInfoVO> mergePageResult = listGPMallOrgOrder(vo);
                result.setMergePageResult(mergePageResult);
                break;
            //非订单合并的电子卖场分页
            case NO:
                PageResult<GPMallPageRespVO> unmergePageResult = listGPMallOrder(vo);
                result.setUnmergePageResult(unmergePageResult);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 电子卖场订单回显起草信息 单位端( 可配置合并或非合并订单)(HLJ_pro)
     *
     * @param orderIdListVO
     * @return <{@link ContractDataDTO }>
     * @throws Exception
     */
    @Override
    public ContractDataDTO queryGPMallOrder4Draft(OrderIdListVO orderIdListVO) throws Exception {
        //查询配置结果是否合并订单
        String value = systemConfigApi.getConfigByKey(IF_ORDER_MERGE.getKey());
        if (StringUtils.isBlank(value)) {
            throw exception(DIY_ERROR, "该用户没有完成订单合并配置，请找管理员配置");
        }
        ContractDataDTO result = new ContractDataDTO();
        result.setIfMergeOrder(value);
        IfEnums ifEnums = IfEnums.getInstance(value);
        switch (Objects.requireNonNull(ifEnums, "订单合并配置错误")) {
            //订单合并的电子卖场
            case YES:
                result = queryByOrgOrderV3(orderIdListVO);
                break;
            //非订单合并的电子卖场
            case NO:
                result = gpMallContractService.queryByOrderIdV4(orderIdListVO.getIdList().get(0), orderIdListVO.getContractFrom());
                break;
            default:
                break;
        }
        return result;
    }

}
