package com.yaoan.module.econtract.job.model;

import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.service.model.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 更新模板有效状态
 */
@Slf4j
@Component("EffectiveJob")
public class EffectiveJob implements JobHandler {
    @Resource
    private ModelService modelService;
    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.debug("开始执行模板有效期更新任务～");
        modelService.updateModelTimeEffect();
        return "模板有效期更新完成";
    }
}
