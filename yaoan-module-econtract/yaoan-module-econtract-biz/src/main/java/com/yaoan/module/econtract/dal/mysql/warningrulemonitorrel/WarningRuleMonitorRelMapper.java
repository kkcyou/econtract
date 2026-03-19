package com.yaoan.module.econtract.dal.mysql.warningrulemonitorrel;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 预警规则与监控项关联关系表（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningRuleMonitorRelMapper extends BaseMapperX<WarningRuleMonitorRelDO> {

    default PageResult<WarningRuleMonitorRelDO> selectPage(WarningRuleMonitorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningRuleMonitorRelDO>()
                .eqIfPresent(WarningRuleMonitorRelDO::getRuleId, reqVO.getRuleId())
                .eqIfPresent(WarningRuleMonitorRelDO::getMonitorId, reqVO.getMonitorId())
                .betweenIfPresent(WarningRuleMonitorRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningRuleMonitorRelDO::getId));
    }

    default List<WarningRuleMonitorRelDO> selectList(WarningRuleMonitorExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningRuleMonitorRelDO>()
                .eqIfPresent(WarningRuleMonitorRelDO::getRuleId, reqVO.getRuleId())
                .eqIfPresent(WarningRuleMonitorRelDO::getMonitorId, reqVO.getMonitorId())
                .betweenIfPresent(WarningRuleMonitorRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningRuleMonitorRelDO::getId));
    }

    @Update("<script>"
            + "UPDATE ecms_warning_rule_monitor_rel "
            + "SET deleted = 1 "
            + "WHERE config_id = #{configId}"
            + "</script>")
    void deleteBatch(@Param("configId")  String configId);}
