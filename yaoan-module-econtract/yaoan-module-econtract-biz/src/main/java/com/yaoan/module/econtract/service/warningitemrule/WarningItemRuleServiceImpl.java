package com.yaoan.module.econtract.service.warningitemrule;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningitemrule.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningitemrule.WarningItemRuleConvert;
import com.yaoan.module.econtract.dal.mysql.warningitemrule.WarningItemRuleMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 预警规则（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningItemRuleServiceImpl implements WarningItemRuleService {

    @Resource
    private WarningItemRuleMapper warningItemRuleMapper;

    @Override
    public String createWarningItemRule(WarningItemRuleCreateReqVO createReqVO) {
        // 插入
        WarningItemRuleDO warningItemRule = WarningItemRuleConvert.INSTANCE.convert(createReqVO);
        warningItemRuleMapper.insert(warningItemRule);
        // 返回
        return warningItemRule.getId();
    }

    @Override
    public void updateWarningItemRule(WarningItemRuleUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningItemRuleExists(updateReqVO.getId());
        // 更新
        WarningItemRuleDO updateObj = WarningItemRuleConvert.INSTANCE.convert(updateReqVO);
        warningItemRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningItemRule(String id) {
        // 校验存在
        validateWarningItemRuleExists(id);
        // 删除
        warningItemRuleMapper.deleteById(id);
    }

    private void validateWarningItemRuleExists(String id) {
        if (warningItemRuleMapper.selectById(id) == null) {
//            throw exception(WARNING_ITEM_RULE_NOT_EXISTS);
        }
    }

    @Override
    public WarningItemRuleDO getWarningItemRule(String id) {
        return warningItemRuleMapper.selectById(id);
    }

    @Override
    public List<WarningItemRuleDO> getWarningItemRuleList(Collection<String> ids) {
        return warningItemRuleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningItemRuleDO> getWarningItemRulePage(WarningItemRulePageReqVO pageReqVO) {
        return warningItemRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningItemRuleDO> getWarningItemRuleList(WarningItemRuleExportReqVO exportReqVO) {
        return warningItemRuleMapper.selectList(exportReqVO);
    }

}
