package com.yaoan.module.bpm.service.task.listener.borrow;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.borrow.BorrowBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
@Slf4j
public class BorrowBackExecuteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        BorrowBpmApi bpmApi = SpringUtil.getBean(BorrowBpmApi.class);
        log.info("触发了借阅退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
