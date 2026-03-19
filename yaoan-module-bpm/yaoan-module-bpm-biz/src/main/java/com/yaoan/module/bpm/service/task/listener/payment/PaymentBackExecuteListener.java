package com.yaoan.module.bpm.service.task.listener.payment;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.payment.PaymentApplicationBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 支付申请退回执行监听器
 *
 * @author doujiale
 */
@Slf4j
public class PaymentBackExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = -394195158683929490L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        PaymentApplicationBpmApi paymentApi = SpringUtil.getBean(PaymentApplicationBpmApi.class);
        log.info("触发了支付申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        paymentApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
