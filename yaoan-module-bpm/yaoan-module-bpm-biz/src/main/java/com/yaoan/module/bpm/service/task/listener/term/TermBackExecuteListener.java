package com.yaoan.module.bpm.service.task.listener.term;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.term.TermBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 条款第一审批人驳回退回执行监听器
 * @author: Pele
 * @date: 2024/1/10 15:16
 */
@Slf4j
public class TermBackExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = 1724522825549001896L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        TermBpmApi bpmApi = SpringUtil.getBean(TermBpmApi.class);
        log.info("触发了申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
