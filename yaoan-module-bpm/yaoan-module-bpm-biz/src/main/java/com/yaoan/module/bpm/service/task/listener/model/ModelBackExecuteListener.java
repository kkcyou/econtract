package com.yaoan.module.bpm.service.task.listener.model;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.model.ModelBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 模板申请退回执行监视器
 * @author: Pele
 * @date: 2024/1/15 10:24
 */
@Slf4j
public class ModelBackExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = 1675993017408800915L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ModelBpmApi api = SpringUtil.getBean(ModelBpmApi.class);
        log.info("触发了模板申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        api.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
