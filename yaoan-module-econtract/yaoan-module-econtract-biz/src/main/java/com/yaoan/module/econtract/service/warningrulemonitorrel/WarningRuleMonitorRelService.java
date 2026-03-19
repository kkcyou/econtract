package com.yaoan.module.econtract.service.warningrulemonitorrel;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警规则与监控项关联关系表（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningRuleMonitorRelService {

    /**
     * 创建预警规则与监控项关联关系表（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningRuleMonitor(@Valid WarningRuleMonitorCreateReqVO createReqVO);

    /**
     * 更新预警规则与监控项关联关系表（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningRuleMonitor(@Valid WarningRuleMonitorUpdateReqVO updateReqVO);

    /**
     * 删除预警规则与监控项关联关系表（new预警）
     *
     * @param id 编号
     */
    void deleteWarningRuleMonitor(String id);

    /**
     * 获得预警规则与监控项关联关系表（new预警）
     *
     * @param id 编号
     * @return 预警规则与监控项关联关系表（new预警）
     */
    WarningRuleMonitorRelDO getWarningRuleMonitor(String id);

    /**
     * 获得预警规则与监控项关联关系表（new预警）列表
     *
     * @param ids 编号
     * @return 预警规则与监控项关联关系表（new预警）列表
     */
    List<WarningRuleMonitorRelDO> getWarningRuleMonitorList(Collection<String> ids);

    /**
     * 获得预警规则与监控项关联关系表（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警规则与监控项关联关系表（new预警）分页
     */
    PageResult<WarningRuleMonitorRelDO> getWarningRuleMonitorPage(WarningRuleMonitorPageReqVO pageReqVO);

    /**
     * 获得预警规则与监控项关联关系表（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警规则与监控项关联关系表（new预警）列表
     */
    List<WarningRuleMonitorRelDO> getWarningRuleMonitorList(WarningRuleMonitorExportReqVO exportReqVO);

}
