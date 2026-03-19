package com.yaoan.module.econtract.dal.mysql.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.TradingContractPageRepVO;
import com.yaoan.module.econtract.controller.admin.warning.vo.ContractWarningReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION;
import static com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/20 15:56
 */
@Mapper
public interface TradingContractExtMapper extends BaseMapperX<TradingContractExtDO> {
    /**
     * 电子交易列表
     */
    default PageResult<TradingContractExtDO> pageGPXTradingList(TradingContractPageRepVO contractPageReqVO) {
        /**
         * /**
         *      * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
         *      */
//         甲方指派联系人" buyerLink;
//        代理机构" agencyName
//          packageGuid;
//         包名称 packageName;
//         包号 packageNumber;
//         包预算 :amount;
        MPJLambdaWrapper<TradingContractExtDO> TradingContractExtDOMPJLambdaWrapper = new MPJLambdaWrapper<TradingContractExtDO>()
                .select(TradingContractExtDO::getId, TradingContractExtDO::getBidGuid, TradingContractExtDO::getBuyerLink, TradingContractExtDO::getTotalMoney, TradingContractExtDO::getCode, TradingContractExtDO::getName, TradingContractExtDO::getStatus
                        , TradingContractExtDO::getBuyerOrgName, TradingContractExtDO::getSupplierName, TradingContractExtDO::getPerformEndDate, TradingContractExtDO::getPerformStartDate, TradingContractExtDO::getProcessInstanceId
                        , TradingContractExtDO::getPlatform, TradingContractExtDO::getIsBak, TradingContractExtDO::getContractSignTime, TradingContractExtDO::getRegionName, TradingContractExtDO::getBuyerLink, TradingContractExtDO::getSupplierName, TradingContractExtDO::getBuyerLegalPerson,
                        TradingContractExtDO::getCategory, TradingContractExtDO::getRemark9, TradingContractExtDO::getCategoryName, TradingContractExtDO::getProjectGuid)
                .selectAs(PackageInfoDO::getAmount, TradingContractExtDO::getPerformanceMoney)
                .selectAs(PackageInfoDO::getPackageName, TradingContractExtDO::getBankName)
                .selectAs(PackageInfoDO::getPackageNumber, TradingContractExtDO::getPropertyServiceType)
                .selectAs(PackageInfoDO::getPurchaseMethodName, TradingContractExtDO::getAgreementCode)
                .selectAs(PackageInfoDO::getZoneName, TradingContractExtDO::getRegionName)
                .leftJoin(PackageInfoDO.class, PackageInfoDO::getPackageGuid, TradingContractExtDO::getBidGuid)
                .eq(TradingContractExtDO::getPlatform, PlatformEnums.GPMS_GPX.getCode())
                .eq(ObjectUtil.isNotEmpty(contractPageReqVO.getStatus()), TradingContractExtDO::getStatus, contractPageReqVO.getStatus())
                .orderByDesc(TradingContractExtDO::getUpdateTime, TradingContractExtDO::getCreateTime);
        //筛选出当前登录的代理机构负责审核的
//                .eq(TradingContractExtDO::getAgentGuid,String.valueOf(SecurityFrameworkUtils.getLoginUserId()))
        //采购单位idList
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getOrgIdList())) {
            TradingContractExtDOMPJLambdaWrapper.in(TradingContractExtDO::getBuyerOrgId, contractPageReqVO.getOrgIdList());
        }
        //计划id
        if (ObjectUtil.isNotNull(contractPageReqVO.getBuyPlanId())) {
            TradingContractExtDOMPJLambdaWrapper.eq(TradingContractExtDO::getBuyPlanId, contractPageReqVO.getBuyPlanId());
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getCode())) {
            TradingContractExtDOMPJLambdaWrapper.eq(TradingContractExtDO::getCode, contractPageReqVO.getCode());
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getBuyerOrgName())) {
            TradingContractExtDOMPJLambdaWrapper.like(TradingContractExtDO::getBuyerOrgName, contractPageReqVO.getBuyerOrgName());
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getSupplierName())) {
            TradingContractExtDOMPJLambdaWrapper.like(TradingContractExtDO::getSupplierName, contractPageReqVO.getSupplierName());
        }
        //项目类型名称
        if (ObjectUtil.isNotNull(contractPageReqVO.getName())) {
            TradingContractExtDOMPJLambdaWrapper.like(TradingContractExtDO::getName, contractPageReqVO.getName());
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getProjectTypeName())) {
            TradingContractExtDOMPJLambdaWrapper.like(PackageInfoDO::getProjectTypeName, contractPageReqVO.getProjectTypeName());
        }
        //所属的项目guid
        if (ObjectUtil.isNotNull(contractPageReqVO.getProjectGuid())) {
            TradingContractExtDOMPJLambdaWrapper.eq(TradingContractExtDO::getProjectGuid, contractPageReqVO.getProjectGuid());
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getPackageName())) {
            TradingContractExtDOMPJLambdaWrapper.like(PackageInfoDO::getPackageName, contractPageReqVO.getPackageName());
        }
        return selectPage(contractPageReqVO, TradingContractExtDOMPJLambdaWrapper);

    }

    /**
     * 交易执行-找出已备案的合同
     */
    default List<TradingContractExtDO> selectBackedContracts(List<String> planIds) {
        return selectList(new LambdaQueryWrapperX<TradingContractExtDO>()
                .inIfPresent(TradingContractExtDO::getBuyPlanId, planIds)
                //备案状态的合同，才算是已签约金额
                .eq(TradingContractExtDO::getStatus, CONTRACT_AUDITSTATUS_RECORDED.getCode())
        );

    }

    /**
     * 查询非作废状态的所有合同
     * 在途接口专用
     */
    default List<TradingContractExtDO> getContractsBuPackageId(String packageId) {
        JsonObject jsonObject = JsonParser.parseString(packageId).getAsJsonObject();
        return selectList(new LambdaQueryWrapperX<TradingContractExtDO>()
                .select(TradingContractExtDO::getId).orderByDesc(TradingContractExtDO::getUpdateTime)
                .ne(TradingContractExtDO::getStatus, CONTRACT_AUDITSTATUS_CANCELLATION.getCode())
                .eq(TradingContractExtDO::getBuyPlanPackageId, jsonObject.get("packageId").getAsString()));
    }

    Long selectSignedQuantity(@Param("reqVO") ContractWarningReqVO reqVO);


    default void getQueryCriteria(ContractWarningReqVO reqVO, StringBuilder subQuery) {


        if (ObjectUtil.isNotEmpty(reqVO.getRegionCode())) {
            subQuery.append(" AND zone_code = '").append(reqVO.getRegionCode()).append("'");
        }
        if (ObjectUtil.isNotEmpty(reqVO.getOrgId())) {
            subQuery.append(" AND purchaser_org_ids = '").append(reqVO.getOrgId()).append("'");
        }

    }

    Long selectSignedOverdueQuantity(@Param("reqVO") ContractWarningReqVO reqVO);


    Long selectSignedNoOverdueQuantity(@Param("reqVO") ContractWarningReqVO reqVO);


    default List<TradingContractExtDO> selectPackageContractList(List<String> packageGuids) {
        LambdaQueryWrapperX<TradingContractExtDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(TradingContractExtDO::getOrderId, TradingContractExtDO::getBuyPlanPackageId, TradingContractExtDO::getStatus, TradingContractExtDO::getSupSinTime, TradingContractExtDO::getOrgSinTime, TradingContractExtDO::getPlatform,TradingContractExtDO::getContractSignTime);
        wrapper.in(TradingContractExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
        if (CollectionUtil.isNotEmpty(packageGuids)) {
            wrapper.inIfPresent(TradingContractExtDO::getBuyPlanPackageId, packageGuids);

        }
        return selectList(wrapper);
    }

    /**
     * 查询非作废状态的所有合同
     */
    default List<TradingContractExtDO> getContractsPackageId(GPXContractCreateReqVO contractCreateReqVO) {
        MPJLambdaWrapper<TradingContractExtDO> wrapper = new MPJLambdaWrapper<TradingContractExtDO>()
                .selectAll(TradingContractExtDO.class).orderByDesc(TradingContractExtDO::getUpdateTime)
                .leftJoin(PackageInfoDO.class, PackageInfoDO::getPackageGuid, TradingContractExtDO::getBuyPlanPackageId)
                .ne(TradingContractExtDO::getStatus, CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
        if(StringUtils.isNotBlank(contractCreateReqVO.getPackageId())){
            wrapper.eq(PackageInfoDO::getPackageGuid, contractCreateReqVO.getPackageId());
        }
        //如果是起草后的合同要编辑，则不计算当前合同的
        if(StringUtils.isNotBlank(contractCreateReqVO.getId())){
            wrapper.ne(TradingContractExtDO::getId, contractCreateReqVO.getId());
        }

        return selectList(wrapper);
    }
    default List<TradingContractExtDO> selectOrderContractList( List<String> orderGuid) {
        LambdaQueryWrapperX<TradingContractExtDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(TradingContractExtDO::getOrderId, TradingContractExtDO::getBuyPlanPackageId, TradingContractExtDO::getStatus, TradingContractExtDO::getSupSinTime, TradingContractExtDO::getOrgSinTime, TradingContractExtDO::getPlatform,TradingContractExtDO::getContractSignTime,TradingContractExtDO::getContractDrafter);
        wrapper.in(TradingContractExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
        if (CollectionUtil.isNotEmpty(orderGuid)) {
            wrapper.inIfPresent(TradingContractExtDO::getOrderId, orderGuid);

        }
        return selectList(wrapper);

    }

    /**
     * 在途合同的包id(除了联合采购和一般项目的多供应商包的)
     *
     */
    default List<TradingContractExtDO> selectList4GPX(List<Integer> excludedStatuses) {
        MPJLambdaWrapper<TradingContractExtDO> mpjLambdaWrapper = new MPJLambdaWrapper<TradingContractExtDO>()
                .select(TradingContractExtDO::getId,TradingContractExtDO::getBuyPlanPackageId);
        mpjLambdaWrapper.leftJoin(PackageInfoDO.class, PackageInfoDO::getProjectGuid, TradingContractExtDO::getProjectGuid)
                .eq(TradingContractExtDO::getPlatform, PlatformEnums.GPMS_GPX.getCode())
                .ne(PackageInfoDO::getBiddingMethodCode, BiddingMethodEnums.UNION.getCode())
                .eq(PackageInfoDO::getSupplierType,1)
                .in(TradingContractExtDO::getStatus, excludedStatuses);
        return selectList(mpjLambdaWrapper);
    }
}
