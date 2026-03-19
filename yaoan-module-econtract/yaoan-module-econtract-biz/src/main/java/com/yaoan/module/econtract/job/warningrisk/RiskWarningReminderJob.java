package com.yaoan.module.econtract.job.warningrisk;

import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.service.warningrisk.WarningRiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 风险预警提醒
 */
@Slf4j
@Component("riskWarningReminderJob")
public class RiskWarningReminderJob implements JobHandler {
    @Resource
    private WarningRiskService warningRiskService;
    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.debug("开始执行风险预警提醒任务～");
        warningRiskService.riskAlertReminder();
        return "风险预警提醒任务完成";
    }
}
