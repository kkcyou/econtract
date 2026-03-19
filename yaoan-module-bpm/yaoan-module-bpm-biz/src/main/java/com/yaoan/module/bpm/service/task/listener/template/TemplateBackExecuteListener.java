package com.yaoan.module.bpm.service.task.listener.template;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.template.TemplateBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/15 10:46
 */
@Slf4j
public class TemplateBackExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = -8533320798664466706L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        TemplateBpmApi api = SpringUtil.getBean(TemplateBpmApi.class);
        log.info("触发了范本申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        try {
            api.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("范本审批通过自动转换ofd文件异常。");
        }
    }
}
