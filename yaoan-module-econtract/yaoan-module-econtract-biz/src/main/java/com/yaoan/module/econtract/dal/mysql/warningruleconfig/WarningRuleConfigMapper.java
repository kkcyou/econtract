package com.yaoan.module.econtract.dal.mysql.warningruleconfig;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningRiskPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 预警规则配置 Mapper
 *
 * @author lls
 */
@Mapper
public interface WarningRuleConfigMapper extends BaseMapperX<WarningRuleConfigDO> {
    default PageResult<WarningRuleConfigDO> selectPage(WarningRiskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningRuleConfigDO>()
                .eqIfPresent(WarningRuleConfigDO::getWarningRuleCode, reqVO.getWarningRuleCode())
                .likeIfPresent(WarningRuleConfigDO::getWarningRuleName, reqVO.getWarningRuleName())
                .eqIfPresent(WarningRuleConfigDO::getWarningRuleType, reqVO.getWarningRuleType())
                .eqIfPresent(WarningRuleConfigDO::getWarningRuleRemark, reqVO.getWarningRuleRemark())
                .eqIfPresent(WarningRuleConfigDO::getBusinessTypeId, reqVO.getBusinessTypeId())
                .eqIfPresent(WarningRuleConfigDO::getWarningRuleField, reqVO.getWarningRuleField())
                .eqIfPresent(WarningRuleConfigDO::getEnable, reqVO.getEnable())
                .eqIfPresent(WarningRuleConfigDO::getIsBlocked, reqVO.getIsBlocked())
                .eqIfPresent(WarningRuleConfigDO::getCompanyId, reqVO.getCompanyId())
                .betweenIfPresent(WarningRuleConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningRuleConfigDO::getId));
    }


}