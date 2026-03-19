package com.yaoan.module.econtract.dal.mysql.statistics;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractAuditStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.OrderStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.controller.admin.warning.vo.ContractBakOverInfoRespVO;
import com.yaoan.module.econtract.controller.admin.warning.vo.ContractWarningQueryReqVO;
import com.yaoan.module.econtract.controller.admin.warning.vo.SignOverdueUnitsListRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface StatisticsMapper extends BaseMapperX<StatisticsDO> {

    default long selectTotal(ContractWarningQueryReqVO reqVO) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryCriteria(reqVO, queryWrapper);
        return selectCount(queryWrapper);
    }

    default long selectSignedQuantity(ContractWarningQueryReqVO reqVO) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.isNotNull(StatisticsDO::getContractId);
        queryWrapper.and(wrapper -> wrapper.in(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()).or().in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).eq(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode()));
        queryCriteria(reqVO, queryWrapper);
        return selectCount(queryWrapper);
    }

    default void queryCriteria(ContractWarningQueryReqVO reqVO, LambdaQueryWrapperX<StatisticsDO> queryWrapper) {
        queryWrapper.and(wrapper -> wrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus));
        if (ObjectUtil.isNotEmpty(reqVO.getPlatform())) {
            queryWrapper.eq(StatisticsDO::getPlatform, reqVO.getPlatform());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getRegionCode())) {
            queryWrapper.eq(StatisticsDO::getRegionCode, reqVO.getRegionCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getOrgId())) {
            queryWrapper.eq(StatisticsDO::getOrgId, reqVO.getOrgId());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStartDate())) {
            queryWrapper.ge(StatisticsDO::getWinBidTime, reqVO.getStartDate());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getEndDate())) {
            queryWrapper.le(StatisticsDO::getWinBidTime, reqVO.getEndDate());
        }
    }

    default long selectSignedOverdueQuantity(ContractWarningQueryReqVO reqVO) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(StatisticsDO::getSignFlag, 1);
        queryCriteria(reqVO, queryWrapper);
        return selectCount(queryWrapper);
    }

    default long selectUnSignedOverdueQuantity(ContractWarningQueryReqVO reqVO, LocalDate pastDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(wrapper -> wrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus));
        if (ObjectUtil.isNotEmpty(reqVO.getPlatform())) {
            queryWrapper.eq(StatisticsDO::getPlatform, reqVO.getPlatform());
        } else {
            queryWrapper.in(StatisticsDO::getPlatform, PlatformEnums.GPMS_GPX.getCode(), PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getRegionCode())) {
            queryWrapper.eq(StatisticsDO::getRegionCode, reqVO.getRegionCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getOrgId())) {
            queryWrapper.eq(StatisticsDO::getOrgId, reqVO.getOrgId());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStartDate())) {
            queryWrapper.ge(StatisticsDO::getWinBidTime, reqVO.getStartDate());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getEndDate())) {
            queryWrapper.le(StatisticsDO::getWinBidTime, reqVO.getEndDate());
        }
        queryWrapper.and(wrapper -> wrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()));
        queryWrapper.lt(StatisticsDO::getWinBidTime, pastDate);
        return selectCount(queryWrapper);
    }


    List<StatisticsDO> selectCountList(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("now") LocalDate now);


    default PageResult<StatisticsDO> selectUnSignedOverduePage(ContractWarningQueryReqVO reqVO, LocalDate pastDate, LocalDate date) {
//       LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
//       queryWrapper.and(wrapper ->
//               wrapper.isNull(StatisticsDO::getContractId)
//                       .or().notIn(StatisticsDO::getContractStatus,
//                               HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(),
//                               HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(),
//                               HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())
//                       .in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode())
//                       .notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(),ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode())
//       );
//       queryWrapper.lt(StatisticsDO::getWinBidTime, pastDate);
//       queryCriteria(reqVO, queryWrapper);
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(wrapper -> wrapper.or(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, PlatformEnums.JDMALL.getCode(), PlatformEnums.ZHUBAJIE.getCode()).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode())).lt(StatisticsDO::getWinBidTime, date).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus))).or(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, PlatformEnums.GPMS_GPX.getCode(), PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper -> innerWrapper.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()))).lt(StatisticsDO::getWinBidTime, pastDate)));
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectSignedOverduePage(ContractWarningQueryReqVO reqVO) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(StatisticsDO::getSignFlag, 1).orderByDesc(StatisticsDO::getUpdateTime);
        queryCriteria(reqVO, queryWrapper);
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectUnOverduePage(ContractWarningQueryReqVO reqVO, LocalDate date, LocalDate warningDate, LocalDate endDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(wrapper -> wrapper.eq(StatisticsDO::getWinBidTime, date).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).in(StatisticsDO::getPlatform, PlatformEnums.GPMS_GPX.getCode(), PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper -> innerWrapper.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode())))).or(wrapper -> wrapper.gt(StatisticsDO::getWinBidTime, endDate).le(StatisticsDO::getWinBidTime, warningDate).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).in(StatisticsDO::getPlatform, PlatformEnums.JDMALL.getCode(), PlatformEnums.ZHUBAJIE.getCode()).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode())));

        return selectPage(reqVO, queryWrapper);
    }

    default List<StatisticsDO> getOrderContractDOS(ContractWarningQueryReqVO vo) {
        LambdaQueryWrapperX<StatisticsDO> wrapperX = new LambdaQueryWrapperX<StatisticsDO>();
        wrapperX.and(wrapper -> wrapper.in(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()).or().in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).in(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()));
        wrapperX.select(StatisticsDO::getContractStatus, StatisticsDO::getBakFlag, StatisticsDO::getSignTime, StatisticsDO::getPlatform);
        queryCriteria(vo, wrapperX);
        return selectList(wrapperX);

    }

    default PageResult<StatisticsDO> getOrderContractPage(ContractWarningQueryReqVO vo, Integer flag, LocalDate futureDate, LocalDate beforeWorkDay) {
        //flag:0：超期未备案  1:超期备案   2：超期几天备案
        LambdaQueryWrapperX<StatisticsDO> wrapper = new LambdaQueryWrapperX<StatisticsDO>().orderByDesc(StatisticsDO::getUpdateTime);
        if (ObjectUtil.isNotEmpty(flag) && flag == 0) {
            //0：超期未备案
            wrapper.and(wrapperX -> wrapperX.in(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode()).or().in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).in(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode()));
            wrapper.lt(StatisticsDO::getSignTime, futureDate.atStartOfDay());
        } else if (ObjectUtil.isNotEmpty(flag) && flag == 1) {
            //1:超期备案
            wrapper.eq(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()).eq(StatisticsDO::getBakFlag, 1);
        } else if (ObjectUtil.isNotEmpty(flag) && flag == 2) {
            //2：超期几天备案
            wrapper.and(wrapperX -> wrapperX.in(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode()).or().in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).in(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode()));
            wrapper.gt(StatisticsDO::getSignTime, LocalDateTime.of(beforeWorkDay, LocalTime.MAX)).le(StatisticsDO::getSignTime, LocalDateTime.of(futureDate, LocalTime.MAX));
        } else {
            //查看超期备案和未备案的
            wrapper.and(wrapperX -> wrapperX.and(w -> w.in(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode()).or().in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).in(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode())).lt(StatisticsDO::getSignTime, futureDate.atStartOfDay()).or().eq(StatisticsDO::getBakFlag, 1));
        }
        queryCriteria(vo, wrapper);
        return selectPage(vo, wrapper);
    }


    List<SignOverdueUnitsListRespVO> getOrgNameAndCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("offset") Integer offset, @Param("now") LocalDate now, @Param("warningDate") LocalDate warningDate);

    Integer getSignOverdueUnitsPageCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("now") LocalDate now, @Param("warningDate") LocalDate warningDate);

    List<ContractBakOverInfoRespVO> getPageResult(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("warningDate") LocalDate warningDate, @Param("offset") Integer offset);

    Integer getBakOverdueUnitsPageCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("warningDate") LocalDate warningDate);

    List<StatisticsDO> selectBakCountList(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("warningDate") LocalDate warningDate);

    default PageResult<StatisticsDO> selectOgrOverduePage(ContractWarningQueryReqVO reqVO, LocalDate pastDate, LocalDate warningDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).and(wrapper -> wrapper.and(subWrapper -> subWrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(w -> w.eq(StatisticsDO::getSignFlag, 1).or(innerWrapper -> innerWrapper.or(subInnerWrapper -> subInnerWrapper.in(StatisticsDO::getPlatform, PlatformEnums.JDMALL.getCode(), PlatformEnums.ZHUBAJIE.getCode()).and(subSubInnerWrapper -> subSubInnerWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())).lt(StatisticsDO::getWinBidTime, warningDate).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(subWrapper1 -> subWrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus))).or(subInnerWrapper -> subInnerWrapper.in(StatisticsDO::getPlatform, PlatformEnums.GPMS_GPX.getCode(), PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(subWrapper1 -> subWrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(subSubInnerWrapper -> subSubInnerWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper2 -> innerWrapper2.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()))).lt(StatisticsDO::getWinBidTime, pastDate)))));

        return selectPage(reqVO, queryWrapper);
    }

    default long selectZbjAndJdUnSignedOverdueQuantity(ContractWarningQueryReqVO reqVO, LocalDate warningDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(wrapper -> wrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus));
        if (ObjectUtil.isNotEmpty(reqVO.getPlatform())) {
            queryWrapper.eq(StatisticsDO::getPlatform, reqVO.getPlatform());
        } else {
            queryWrapper.in(StatisticsDO::getPlatform, PlatformEnums.JDMALL.getCode(), PlatformEnums.ZHUBAJIE.getCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getRegionCode())) {
            queryWrapper.eq(StatisticsDO::getRegionCode, reqVO.getRegionCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getOrgId())) {
            queryWrapper.eq(StatisticsDO::getOrgId, reqVO.getOrgId());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStartDate())) {
            queryWrapper.ge(StatisticsDO::getWinBidTime, reqVO.getStartDate());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getEndDate())) {
            queryWrapper.le(StatisticsDO::getWinBidTime, reqVO.getEndDate());
        }
        queryWrapper.and(wrapper -> wrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
        queryWrapper.lt(StatisticsDO::getWinBidTime, warningDate);
        return selectCount(queryWrapper);
    }

    default PageResult<StatisticsDO> selectZbjAndJdUnSignedOverduePage(ContractWarningQueryReqVO reqVO, LocalDate date) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(wrapper -> wrapper.or(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, reqVO.getPlatform()).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())).lt(StatisticsDO::getWinBidTime, date).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus))));
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectOtherUnSignedOverduePage(ContractWarningQueryReqVO reqVO, LocalDate pastDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, reqVO.getPlatform()).apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper -> innerWrapper.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()))).lt(StatisticsDO::getWinBidTime, pastDate));
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectOtherUnOverduePage(ContractWarningQueryReqVO reqVO, LocalDate date) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(StatisticsDO::getWinBidTime, date).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper -> innerWrapper.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode())));
        queryCriteria(reqVO, queryWrapper);
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectZbjAndJdUnOverduePage(ContractWarningQueryReqVO reqVO, LocalDate warningDate, LocalDate endDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.gt(StatisticsDO::getWinBidTime, endDate).le(StatisticsDO::getWinBidTime, warningDate).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()));
        queryCriteria(reqVO, queryWrapper);
        return selectPage(reqVO, queryWrapper);
    }

    List<StatisticsDO> selectZbjAndJdCountList(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("parse") LocalDate parse);

    List<SignOverdueUnitsListRespVO> getZbjAndJdOrgNameAndCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("offset") int offset, @Param("parse") LocalDate parse);

    Integer getZbjAndJdSignOverdueUnitsPageCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("parse") LocalDate parse);

    List<SignOverdueUnitsListRespVO> getOtherOrgNameAndCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("offset") int offset, @Param("pastDate") LocalDate pastDate);

    Integer getOtherSignOverdueUnitsPageCount(@Param("reqVO") ContractWarningQueryReqVO reqVO, @Param("pastDate") LocalDate pastDate);

    default PageResult<StatisticsDO> selectZbjAndJdOrgOverduePage(ContractWarningQueryReqVO reqVO, LocalDate warningDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).and(wrapper1 -> wrapper1.and(subWrapper -> subWrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(w -> w.eq(StatisticsDO::getSignFlag, 1).in(StatisticsDO::getPlatform, reqVO.getPlatform()).or(wrapper -> wrapper.or(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, reqVO.getPlatform()).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or().notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())).lt(StatisticsDO::getWinBidTime, warningDate).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper2 -> wrapper2.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus))))));
        return selectPage(reqVO, queryWrapper);
    }

    default PageResult<StatisticsDO> selectOtherOgrOverduePage(ContractWarningQueryReqVO reqVO, LocalDate pastDate) {
        LambdaQueryWrapperX<StatisticsDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.apply(reqVO.getRegionCode() != null, "region_code = {0}", reqVO.getRegionCode()).apply(reqVO.getOrgId() != null, "org_id = {0}", reqVO.getOrgId()).and(wrapper -> wrapper.and(subWrapper -> subWrapper.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(w -> w.eq(StatisticsDO::getSignFlag, 1).in(StatisticsDO::getPlatform, reqVO.getPlatform()).or(subWrapper -> subWrapper.in(StatisticsDO::getPlatform, reqVO.getPlatform()).ge(ObjectUtil.isNotEmpty(reqVO.getStartDate()), StatisticsDO::getWinBidTime, reqVO.getStartDate()).le(ObjectUtil.isNotEmpty(reqVO.getEndDate()), StatisticsDO::getWinBidTime, reqVO.getEndDate()).and(wrapper1 -> wrapper1.ne(StatisticsDO::getOrderStatus, OrderStatusEnums.CONFIRM_CANCEL.getCode()).or().isNull(StatisticsDO::getOrderStatus)).and(subSubWrapper -> subSubWrapper.isNull(StatisticsDO::getContractId).or(innerWrapper -> innerWrapper.notIn(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()).in(StatisticsDO::getPlatform, PlatformEnums.GPMALL.getCode(), PlatformEnums.GP_GPFA.getCode()).notIn(StatisticsDO::getContractStatus, ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(), ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_RECORD.getCode()))).lt(StatisticsDO::getWinBidTime, pastDate))));
        return selectPage(reqVO, queryWrapper);
    }
}
