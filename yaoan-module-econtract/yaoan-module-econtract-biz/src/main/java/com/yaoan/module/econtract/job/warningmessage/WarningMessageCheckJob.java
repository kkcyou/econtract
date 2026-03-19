package com.yaoan.module.econtract.job.warningmessage;

import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEvent;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import com.yaoan.module.econtract.service.warningcfg.WarningCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 风险预警提醒
 */
@Slf4j
@Component("warningMessageCheckJob")
@Validated
public class WarningMessageCheckJob implements JobHandler {

    @Resource
    private WarningCfgService warningCfgService;

    @Resource
    private EcmsWarningEventPublisher warningEventPublisher;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.debug("开始执行风险预警提醒任务～");
        warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("2"));
        return "风险预警提醒任务完成";
    }
}
