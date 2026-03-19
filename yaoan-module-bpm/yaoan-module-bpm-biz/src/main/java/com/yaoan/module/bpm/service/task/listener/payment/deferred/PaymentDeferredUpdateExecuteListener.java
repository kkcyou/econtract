package com.yaoan.module.bpm.service.task.listener.payment.deferred;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.payment.deferred.PaymentDeferredApplicationBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 10:12
 */
@Slf4j
public class PaymentDeferredUpdateExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        PaymentDeferredApplicationBpmApi paymentApi = SpringUtil.getBean(PaymentDeferredApplicationBpmApi.class);
        log.info("触发了计划延期申请更再次提交任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        paymentApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
