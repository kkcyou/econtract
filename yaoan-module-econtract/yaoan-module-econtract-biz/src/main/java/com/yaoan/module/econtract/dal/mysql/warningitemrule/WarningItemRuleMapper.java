package com.yaoan.module.econtract.dal.mysql.warningitemrule;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningitemrule.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 预警规则（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningItemRuleMapper extends BaseMapperX<WarningItemRuleDO> {

    default PageResult<WarningItemRuleDO> selectPage(WarningItemRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningItemRuleDO>()
                .eqIfPresent(WarningItemRuleDO::getWarningItemId, reqVO.getWarningItemId())
                .likeIfPresent(WarningItemRuleDO::getWarningItemName, reqVO.getWarningItemName())
                .eqIfPresent(WarningItemRuleDO::getMonitorItemId, reqVO.getMonitorItemId())
                .likeIfPresent(WarningItemRuleDO::getMonitorItemName, reqVO.getMonitorItemName())
                .eqIfPresent(WarningItemRuleDO::getCompareType, reqVO.getCompareType())
                .eqIfPresent(WarningItemRuleDO::getCompareItemStart, reqVO.getCompareItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareItemEnd, reqVO.getCompareItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDecItemStart, reqVO.getCompareDecItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareDecItemEnd, reqVO.getCompareDecItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDateItemStart, reqVO.getCompareDateItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareDateItemEnd, reqVO.getCompareDateItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDataType, reqVO.getCompareDataType())
                .betweenIfPresent(WarningItemRuleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningItemRuleDO::getId));
    }

    default List<WarningItemRuleDO> selectList(WarningItemRuleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningItemRuleDO>()
                .eqIfPresent(WarningItemRuleDO::getWarningItemId, reqVO.getWarningItemId())
                .likeIfPresent(WarningItemRuleDO::getWarningItemName, reqVO.getWarningItemName())
                .eqIfPresent(WarningItemRuleDO::getMonitorItemId, reqVO.getMonitorItemId())
                .likeIfPresent(WarningItemRuleDO::getMonitorItemName, reqVO.getMonitorItemName())
                .eqIfPresent(WarningItemRuleDO::getCompareType, reqVO.getCompareType())
                .eqIfPresent(WarningItemRuleDO::getCompareItemStart, reqVO.getCompareItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareItemEnd, reqVO.getCompareItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDecItemStart, reqVO.getCompareDecItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareDecItemEnd, reqVO.getCompareDecItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDateItemStart, reqVO.getCompareDateItemStart())
                .eqIfPresent(WarningItemRuleDO::getCompareDateItemEnd, reqVO.getCompareDateItemEnd())
                .eqIfPresent(WarningItemRuleDO::getCompareDataType, reqVO.getCompareDataType())
                .betweenIfPresent(WarningItemRuleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningItemRuleDO::getId));
    }


    @Update("<script>"
            + "UPDATE ecms_warning_item_rule "
            + "SET deleted = 1 "
            + "WHERE config_id = #{configId}"
            + "</script>")
    void deleteBatch(@Param("configId")  String configId);

}
