package com.yaoan.module.econtract.dal.mysql.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRulePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审查清单-审查规则关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RelChecklistRuleMapper extends BaseMapperX<RelChecklistRuleDO> {

    default PageResult<RelChecklistRuleDO> selectPage(RelChecklistRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RelChecklistRuleDO>()
                .eqIfPresent(RelChecklistRuleDO::getReviewId, reqVO.getReviewId())
                .eqIfPresent(RelChecklistRuleDO::getReviewListId, reqVO.getReviewListId())
                .betweenIfPresent(RelChecklistRuleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RelChecklistRuleDO::getId));
    }

}