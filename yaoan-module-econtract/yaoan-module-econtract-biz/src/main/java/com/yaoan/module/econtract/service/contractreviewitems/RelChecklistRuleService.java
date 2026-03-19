package com.yaoan.module.econtract.service.contractreviewitems;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelCheckListRuleCommonRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRulePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRuleSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.ReviewRulesRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 审查清单-审查规则关联 Service 接口
 *
 * @author 芋道源码
 */
public interface RelChecklistRuleService {

    /**
     * 创建审查清单-审查规则关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRelChecklistRule(@Valid RelChecklistRuleSaveReqVO createReqVO);

    /**
     * 更新审查清单-审查规则关联
     *
     * @param updateReqVO 更新信息
     */
    void updateRelChecklistRule(@Valid RelChecklistRuleSaveReqVO updateReqVO);

    /**
     * 删除审查清单-审查规则关联
     *
     * @param id 编号
     */
    void deleteRelChecklistRule(String id);

    /**
     * 获得审查清单-审查规则关联
     *
     * @param id 编号
     * @return 审查清单-审查规则关联
     */
    RelChecklistRuleDO getRelChecklistRule(String id);

    /**
     * 获得审查清单-审查规则关联分页
     *
     * @param pageReqVO 分页查询
     * @return 审查清单-审查规则关联分页
     */
    PageResult<RelChecklistRuleDO> getRelChecklistRulePage(RelChecklistRulePageReqVO pageReqVO);

    List<RelCheckListRuleCommonRespVO> getRelChecklistRuleByIds(List<String> checkListIds);

    List<ReviewRulesRespVO> getReviewRulesByCheckListIds(List<String> checkListIds);
}