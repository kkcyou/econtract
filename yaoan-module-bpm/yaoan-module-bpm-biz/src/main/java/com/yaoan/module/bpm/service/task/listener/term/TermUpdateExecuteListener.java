package com.yaoan.module.bpm.service.task.listener.term;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.term.TermBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/10 15:18
 */
@Slf4j
public class TermUpdateExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = 338764426133747895L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        TermBpmApi bpmApi = SpringUtil.getBean(TermBpmApi.class);
        log.info("触发了支付申请重新修改任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
