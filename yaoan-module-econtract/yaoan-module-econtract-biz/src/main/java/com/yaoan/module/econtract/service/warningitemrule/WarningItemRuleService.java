package com.yaoan.module.econtract.service.warningitemrule;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningitemrule.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警规则（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningItemRuleService {

    /**
     * 创建预警规则（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningItemRule(@Valid WarningItemRuleCreateReqVO createReqVO);

    /**
     * 更新预警规则（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningItemRule(@Valid WarningItemRuleUpdateReqVO updateReqVO);

    /**
     * 删除预警规则（new预警）
     *
     * @param id 编号
     */
    void deleteWarningItemRule(String id);

    /**
     * 获得预警规则（new预警）
     *
     * @param id 编号
     * @return 预警规则（new预警）
     */
    WarningItemRuleDO getWarningItemRule(String id);

    /**
     * 获得预警规则（new预警）列表
     *
     * @param ids 编号
     * @return 预警规则（new预警）列表
     */
    List<WarningItemRuleDO> getWarningItemRuleList(Collection<String> ids);

    /**
     * 获得预警规则（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警规则（new预警）分页
     */
    PageResult<WarningItemRuleDO> getWarningItemRulePage(WarningItemRulePageReqVO pageReqVO);

    /**
     * 获得预警规则（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警规则（new预警）列表
     */
    List<WarningItemRuleDO> getWarningItemRuleList(WarningItemRuleExportReqVO exportReqVO);

}
