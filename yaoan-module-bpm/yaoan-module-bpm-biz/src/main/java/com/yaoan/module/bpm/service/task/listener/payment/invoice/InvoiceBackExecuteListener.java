package com.yaoan.module.bpm.service.task.listener.payment.invoice;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.payment.invoice.InvoiceApplicationBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 收款申请退回监听
 * @author: Pele
 * @date: 2024/10/10 17:06
 */
@Slf4j
public class InvoiceBackExecuteListener implements ExecutionListener {

    private static final long serialVersionUID = 2010881071391566827L;

    @Override
    public void notify(DelegateExecution execution) {
        InvoiceApplicationBpmApi bpmApi = SpringUtil.getBean(InvoiceApplicationBpmApi.class);
        log.info("触发了收款申请退回任务，BusinessKey:{}", execution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(execution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
