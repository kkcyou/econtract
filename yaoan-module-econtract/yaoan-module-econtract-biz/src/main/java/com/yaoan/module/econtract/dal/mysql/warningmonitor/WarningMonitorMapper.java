package com.yaoan.module.econtract.dal.mysql.warningmonitor;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningmonitor.vo.*;

/**
 * 预警监控项配置表（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningMonitorMapper extends BaseMapperX<WarningMonitorDO> {

    default PageResult<WarningMonitorDO> selectPage(WarningMonitorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningMonitorDO>()
                .eqIfPresent(WarningMonitorDO::getModelId, reqVO.getModelId())
                .likeIfPresent(WarningMonitorDO::getName, reqVO.getName())
                .eqIfPresent(WarningMonitorDO::getType, reqVO.getType())
                .eqIfPresent(WarningMonitorDO::getBusinessCode, reqVO.getBusinessCode())
                .eqIfPresent(WarningMonitorDO::getBusinessField, reqVO.getBusinessField())
                .eqIfPresent(WarningMonitorDO::getFlowKey, reqVO.getFlowKey())
                .eqIfPresent(WarningMonitorDO::getFlowStage, reqVO.getFlowStage())
                .eqIfPresent(WarningMonitorDO::getCompareType, reqVO.getCompareType())
                .eqIfPresent(WarningMonitorDO::getCalculateType, reqVO.getCalculateType())
                .betweenIfPresent(WarningMonitorDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WarningMonitorDO::getCompareStr, reqVO.getCompareStr())
                .orderByDesc(WarningMonitorDO::getId));
    }

    default List<WarningMonitorDO> selectList(WarningMonitorExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningMonitorDO>()
                .eqIfPresent(WarningMonitorDO::getModelId, reqVO.getModelId())
                .likeIfPresent(WarningMonitorDO::getName, reqVO.getName())
                .eqIfPresent(WarningMonitorDO::getType, reqVO.getType())
                .eqIfPresent(WarningMonitorDO::getBusinessCode, reqVO.getBusinessCode())
                .eqIfPresent(WarningMonitorDO::getBusinessField, reqVO.getBusinessField())
                .eqIfPresent(WarningMonitorDO::getFlowKey, reqVO.getFlowKey())
                .eqIfPresent(WarningMonitorDO::getFlowStage, reqVO.getFlowStage())
                .eqIfPresent(WarningMonitorDO::getCompareType, reqVO.getCompareType())
                .eqIfPresent(WarningMonitorDO::getCalculateType, reqVO.getCalculateType())
                .betweenIfPresent(WarningMonitorDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WarningMonitorDO::getCompareStr, reqVO.getCompareStr())
                .orderByDesc(WarningMonitorDO::getId));
    }

}
