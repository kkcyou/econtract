package com.yaoan.module.econtract.dal.mysql.order;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ProjectDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.OrderStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.GCYOrderStatusEnums;
import com.yaoan.module.econtract.enums.order.SortEnums;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:40
 */
@Mapper
public interface GPMallOrderOldMapper extends BaseMapperX<DraftOrderInfoDO> {


    default PageResult<DraftOrderInfoDO> selectPage(GPMallPageReqVO vo, Integer contractDrafter) {

        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .selectAll(DraftOrderInfoDO.class)
//                .eq(DraftOrderInfoDO::getStatus, GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode())
                .orderByAsc(DraftOrderInfoDO::getStatus)
                .orderByDesc(DraftOrderInfoDO::getUpdateTime);

        //已取消的订单不可起草合同
//                .notIn(DraftOrderInfoDO::getOrderStatus, "-1");
        Integer orderStatus = 2;
        if (PlatformEnums.JDMALL.getCode().equals(vo.getContractFrom()) || PlatformEnums.ZHUBAJIE.getCode().equals(vo.getContractFrom())) {
            orderStatus = 1;
        }
        if ("0".equals(String.valueOf(vo.getFlag()))) {
            //待起草
            mpjLambdaWrapper
                    // 合同起草时，要查询出待起草的订单和供应商起草中的订单，按状态status查待起草和已起草，再排除掉单位端已有合同关联的订单notIn（orderIdList）
//                    .eq(DraftOrderInfoDO::getStatus, com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode())
                    .gt(DraftOrderInfoDO::getOrderStatus,0)
                    .le(DraftOrderInfoDO::getOrderStatus, 98)
                    .notIn(CollectionUtil.isNotEmpty(vo.getOrderIdList()),DraftOrderInfoDO::getOrderGuid,vo.getOrderIdList());
//            mpjLambdaWrapper.leftJoin(OrderContractDO.class, OrderContractDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
//                    .and(qu -> qu.isNull(OrderContractDO::getId).or().eq(OrderContractDO::getRemark9, "99")
//                            .or().in(OrderContractDO::getStatus, CollectionUtil.newArrayList(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(),
//                                    HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(),HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode())));
//                    .eq(DraftOrderInfoDO::getOrderStatus,orderStatus);

            // ！！！ 查询服务工程超市待起草订单时，只查待起草的订单,否则查询待起草和供应商起草中的订单
            if(PlatformEnums.ZHUBAJIE.getCode().equals(vo.getContractFrom())){
                mpjLambdaWrapper.eq(DraftOrderInfoDO::getStatus, com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
            } else {
                mpjLambdaWrapper .in(DraftOrderInfoDO::getStatus, Arrays.asList(com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode(),com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.DRAFTED.getCode()));
            }
        }
        if (1 == vo.getFlag()) {
            //已起草
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getStatus, com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.DRAFTED.getCode());
        }
        if (2 == vo.getFlag()) {
            //已取消
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode());
        }
        //排序
        com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums payTimeEnums = com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.getInstance(vo.getPayTimeSort());
        com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums totalAmountEnums = com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.getInstance(vo.getTotalAmountSort());
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.ASCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getPayTime);
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.DESCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getPayTime);
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.ASCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.DESCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        // 商品名称
        if (StringUtils.isNotBlank(vo.getProductName())) {
            mpjLambdaWrapper.leftJoin(GoodsDO.class, GoodsDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                    .like(GoodsDO::getGoodsClassName, vo.getProductName());
        }
        // 订单编码
        if (StringUtils.isNotBlank(vo.getOrderCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getOrderCode, vo.getOrderCode());
        }
        // 采购人
        if (StringUtils.isNotBlank(vo.getPurchaserOrgName())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getPurchaserOrg, vo.getPurchaserOrgName());
        }
        // 下单时间
        if (vo.getPayTime0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getPayTime, vo.getPayTime0(), vo.getPayTime1());
        }
        // 订单总额
        if (vo.getOrderTotalAmount0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getOrderTotalAmount, vo.getOrderTotalAmount0(), vo.getOrderTotalAmount1());
        }
        // 订单类型.
        if (StringUtils.isNotBlank(vo.getOrderType())) {
            mpjLambdaWrapper.leftJoin(ProjectDO.class, ProjectDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                    .eq(ProjectDO::getProjectCategoryCode, vo.getOrderType());
        }
        //计划编号
        if (StringUtils.isNotBlank(vo.getBuyPlanCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getBuyPlanCode, vo.getBuyPlanCode());
        }
        //计划名称
        if (StringUtils.isNotBlank(vo.getBuyPlanName())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getBuyPlanName, vo.getBuyPlanName());
        }
        //平台来源
        if (StringUtils.isNotBlank(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractFrom, vo.getContractFrom());
        }
        //合同起草方
//        if (ObjectUtils.isNotEmpty(contractDrafter) && PlatformEnums.GP_GPFA.getCode().equals(vo.getContractFrom())) {
//            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractDrafter, contractDrafter);
//        }
        //只看本单位的
        if(StringUtils.isNotBlank(vo.getOrgId())){
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getPurchaserOrgGuid,vo.getOrgId());
        }
        if(StringUtils.isNotBlank(vo.getSupplierName())){
            mpjLambdaWrapper.like(DraftOrderInfoDO::getSupplierName,vo.getSupplierName());
        }
        return selectPage(vo, mpjLambdaWrapper);
    }

    default PageResult<DraftOrderInfoDO> selectPage(GPMallPageReqVO vo) {

        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>().selectAll(DraftOrderInfoDO.class).eq(DraftOrderInfoDO::getStatus, GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode()).eq(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode());
        //排序
        SortEnums payTimeEnums = SortEnums.getInstance(vo.getPayTimeSort());
        SortEnums totalAmountEnums = SortEnums.getInstance(vo.getTotalAmountSort());
        if (SortEnums.ASCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getPayTime);
        }
        if (SortEnums.DESCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getPayTime);
        }
        if (SortEnums.ASCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        if (SortEnums.DESCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        // 商品名称
        if (StringUtils.isNotBlank(vo.getProductName())) {
            mpjLambdaWrapper.leftJoin(GoodsDO.class, GoodsDO::getOrderId, DraftOrderInfoDO::getOrderGuid).like(GoodsDO::getGoodsClassName, vo.getProductName());
        }
        // 订单编码
        if (StringUtils.isNotBlank(vo.getOrderCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getOrderCode, vo.getOrderCode());
        }
        // 采购人
        if (StringUtils.isNotBlank(vo.getPurchaserOrgName())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getPurchaserOrg, vo.getPurchaserOrgName());
        }
        // 下单时间
        if (vo.getPayTime0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getPayTime, vo.getPayTime0(), vo.getPayTime1());
        }
        // 订单总额
        if (vo.getOrderTotalAmount0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getOrderTotalAmount, vo.getOrderTotalAmount0(), vo.getOrderTotalAmount1());
        }
        // 订单类型.
        if (StringUtils.isNotBlank(vo.getOrderType())) {
            mpjLambdaWrapper.leftJoin(ProjectDO.class, ProjectDO::getOrderId, DraftOrderInfoDO::getOrderGuid).eq(ProjectDO::getProjectCategoryCode, vo.getOrderType());
        }
        // 订单分类
        if (StringUtils.isNotBlank(vo.getOrderCategory())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getOrderCategory, vo.getOrderCategory());
        }

        //计划编号
        if (StringUtils.isNotBlank(vo.getBuyPlanCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getBuyPlanCode, vo.getBuyPlanCode());
        }
        if (StringUtils.isNotBlank(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractFrom, vo.getContractFrom());
        }

        return selectPage(vo, mpjLambdaWrapper);


    }

    default PageResult<DraftOrderInfoDO> listThirdData(GPMallPageReqVO vo) {
        return null;

    }


    default PageResult<DraftOrderInfoDO> selectPage2(GPMallPageReqVO vo, Long companyId) {

        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>().selectAll(DraftOrderInfoDO.class).eq(DraftOrderInfoDO::getStatus, GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
        //排序
        SortEnums payTimeEnums = SortEnums.getInstance(vo.getPayTimeSort());
        SortEnums totalAmountEnums = SortEnums.getInstance(vo.getTotalAmountSort());
        if (SortEnums.ASCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getPayTime);
        }
        if (SortEnums.DESCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getPayTime);
        }
        if (SortEnums.ASCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByAsc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        if (SortEnums.DESCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByDesc(DraftOrderInfoDO::getOrderTotalAmount);
        }
        // 商品名称
        if (StringUtils.isNotBlank(vo.getProductName())) {
            mpjLambdaWrapper.leftJoin(GoodsDO.class, GoodsDO::getOrderId, DraftOrderInfoDO::getOrderGuid).like(GoodsDO::getGoodsClassName, vo.getProductName());
        }
        // 订单编码
        if (StringUtils.isNotBlank(vo.getOrderCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getOrderCode, vo.getOrderCode());
        }
        // 采购人
        if (StringUtils.isNotBlank(vo.getPurchaserOrgName())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getPurchaserOrg, vo.getPurchaserOrgName());
        }
        // 下单时间
        if (vo.getPayTime0() != null) {
        }
        // 订单总额
        if (vo.getOrderTotalAmount0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getOrderTotalAmount, vo.getOrderTotalAmount0(), vo.getOrderTotalAmount1());
        }
        // 订单类型.
        if (StringUtils.isNotBlank(vo.getOrderType())) {
            mpjLambdaWrapper.leftJoin(ProjectDO.class, ProjectDO::getOrderId, DraftOrderInfoDO::getOrderGuid).eq(ProjectDO::getProjectCategoryCode, vo.getOrderType());
        }
        //计划编号
        if (StringUtils.isNotBlank(vo.getBuyPlanCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getBuyPlanCode, vo.getBuyPlanCode());
        }
        //平台来源
        if (StringUtils.isNotBlank(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractFrom, vo.getContractFrom());
        }
        //合同起草方
        if (ObjectUtils.isNotEmpty(companyId) && PlatformEnums.GP_GPFA.getCode().equals(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getCompanyId, companyId);
        }

        return selectPage(vo, mpjLambdaWrapper);
    }


    default PageResult<DraftOrderInfoDO> selectOrgPage(GPMallPageReqVO vo, Integer contractDrafter) {
        String orgId = SecurityFrameworkUtils.getLoginUser().getOrgId();
        // 创建 MPJLambdaWrapper
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<>();

        mpjLambdaWrapper.select(DraftOrderInfoDO::getBuyPlanCode, DraftOrderInfoDO::getSupplierName)
                .selectMax(DraftOrderInfoDO::getUpdateTime, "updateTime")
                .selectMax(DraftOrderInfoDO::getPayTime, "payTime")
                .selectMax(DraftOrderInfoDO::getOrderTotalAmount, "orderTotalAmount")
                .groupBy(DraftOrderInfoDO::getBuyPlanCode, DraftOrderInfoDO::getSupplierName);
        //只能看到本单位的订单
        mpjLambdaWrapper.eq(DraftOrderInfoDO::getPurchaserOrgGuid, orgId);
        mpjLambdaWrapper.orderByDesc("updateTime");
        //排序
        com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums payTimeEnums = com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.getInstance(vo.getPayTimeSort());
        com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums totalAmountEnums = com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.getInstance(vo.getTotalAmountSort());
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.ASCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByAsc("payTime");
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.DESCENDING_SORT == payTimeEnums) {
            mpjLambdaWrapper.orderByDesc("payTime");
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.ASCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByAsc("orderTotalAmount");
        }
        if (com.yaoan.module.econtract.enums.gcy.gpmall.SortEnums.DESCENDING_SORT == totalAmountEnums) {
            mpjLambdaWrapper.orderByDesc("orderTotalAmount");
        }
        //已取消的订单不可起草合同
//                .notIn(DraftOrderInfoDO::getOrderStatus, "-1");
        Integer orderStatus = 2;
        if (PlatformEnums.JDMALL.getCode().equals(vo.getContractFrom()) || PlatformEnums.ZHUBAJIE.getCode().equals(vo.getContractFrom())) {
            orderStatus = 1;
        }
        if (0 == vo.getFlag()) {
            //待起草
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getStatus, com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode())
                    .gt(DraftOrderInfoDO::getOrderStatus, 0).le(DraftOrderInfoDO::getOrderStatus, 98)
                    .notIn(CollectionUtil.isNotEmpty(vo.getOrderIdList()), DraftOrderInfoDO::getOrderGuid, vo.getOrderIdList());
        }
        if (1 == vo.getFlag()) {
            //已起草
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getStatus, com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.DRAFTED.getCode());
        }
        if (2 == vo.getFlag()) {
            //已取消
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode());
        }
        // 商品名称
        if (StringUtils.isNotBlank(vo.getProductName())) {
            mpjLambdaWrapper.leftJoin(GoodsDO.class, GoodsDO::getOrderId, DraftOrderInfoDO::getOrderGuid).like(GoodsDO::getGoodsClassName, vo.getProductName());
        }
        // 订单编码
        if (StringUtils.isNotBlank(vo.getOrderCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getOrderCode, vo.getOrderCode());
        }
        // 采购人
        if (StringUtils.isNotBlank(vo.getPurchaserOrgName())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getPurchaserOrg, vo.getPurchaserOrgName());
        }
        // 下单时间
        if (vo.getPayTime0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getPayTime, vo.getPayTime0(), vo.getPayTime1());
        }
        // 订单总额
        if (vo.getOrderTotalAmount0() != null) {
            mpjLambdaWrapper.between(DraftOrderInfoDO::getOrderTotalAmount, vo.getOrderTotalAmount0(), vo.getOrderTotalAmount1());
        }
        // 订单类型.
        if (StringUtils.isNotBlank(vo.getOrderType())) {
            mpjLambdaWrapper.leftJoin(ProjectDO.class, ProjectDO::getOrderId, DraftOrderInfoDO::getOrderGuid).eq(ProjectDO::getProjectCategoryCode, vo.getOrderType());
        }
        //计划编号
        if (StringUtils.isNotBlank(vo.getBuyPlanCode())) {
            mpjLambdaWrapper.like(DraftOrderInfoDO::getBuyPlanCode, vo.getBuyPlanCode());
        }
        //平台来源
        if (StringUtils.isNotBlank(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractFrom, vo.getContractFrom());
        }
        //合同起草方
        if (ObjectUtils.isNotEmpty(contractDrafter) && PlatformEnums.GP_GPFA.getCode().equals(vo.getContractFrom())) {
            mpjLambdaWrapper.eq(DraftOrderInfoDO::getContractDrafter, contractDrafter);
        }
        return selectPage(vo, mpjLambdaWrapper);
    }
    default List<DraftOrderInfoDO> getOrdersByContractId0(String contractId) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .eq(ContractOrderRelDO::getContractId, contractId)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getStatus, DraftOrderInfoDO::getOrderStatus,DraftOrderInfoDO::getUpdateTime).orderByDesc(DraftOrderInfoDO::getUpdateTime)
                .distinct();
        return selectList(mpjLambdaWrapper);
    }


    default List<DraftOrderInfoDO> getOrdersByContractId(String contractId) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .eq(ContractOrderRelDO::getContractId, contractId)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getStatus, DraftOrderInfoDO::getOrderStatus)
                .distinct();
        mpjLambdaWrapper.eq(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode());

        return selectList(mpjLambdaWrapper);
    }

    default List<DraftOrderInfoDO> getOrdersByContractId2(String contractId) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .eq(ContractOrderRelDO::getContractId, contractId)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getStatus, DraftOrderInfoDO::getOrderStatus,DraftOrderInfoDO::getUpdateTime).orderByDesc(DraftOrderInfoDO::getUpdateTime)
                .distinct();
        mpjLambdaWrapper.eq(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode());

        return selectList(mpjLambdaWrapper);
    }

    default List<DraftOrderInfoDO> selectOne4Check(UploadContractCreateReqVO vo) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getStatus, DraftOrderInfoDO::getOrderStatus,DraftOrderInfoDO::getCreateTime,
                        DraftOrderInfoDO::getPurchaserOrgGuid,DraftOrderInfoDO::getPurchaserOrg,DraftOrderInfoDO::getSupplierGuid,DraftOrderInfoDO::getSupplierName,DraftOrderInfoDO::getOrderCode,
                        DraftOrderInfoDO::getProjectCategoryCode)
                .distinct();
        mpjLambdaWrapper.in(DraftOrderInfoDO::getOrderGuid, vo.getOrderIdList()).orderByDesc(DraftOrderInfoDO::getCreateTime).last(" limit 1");
        return selectList(mpjLambdaWrapper);
    }

    default List<DraftOrderInfoDO> getOrdersByContractIds1(List<String> contractIds) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .in(ContractOrderRelDO::getContractId, contractIds)
                .selectAs(ContractOrderRelDO::getContractId, DraftOrderInfoDO::getOrglinkFax)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getSupplierName)

                .distinct();

        return selectList(mpjLambdaWrapper);
    }

    default List<DraftOrderInfoDO> selectListByContractIdAndOrderIds(String contractId, List<String> orderIdList) {
        MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                .eq(ContractOrderRelDO::getContractId, contractId)
                .in(ContractOrderRelDO::getOrderId, orderIdList)
                .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getSupplierName)

                .distinct();

        return selectList(mpjLambdaWrapper);
    }

   default List<DraftOrderInfoDO> selectOrdersByContractId(String contractId){
       MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
               .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
               .eq(ContractOrderRelDO::getContractId, contractId)
               .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getSupplierName)
               .distinct();

       return selectList(mpjLambdaWrapper);
   }
}
