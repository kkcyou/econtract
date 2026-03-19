package com.yaoan.module.bpm.service.task.listener.payment.invoice;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.payment.invoice.InvoiceApplicationBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 收款申请再次提交监听
 * @author: Pele
 * @date: 2024/10/10 17:07
 */
@Slf4j
public class InvoiceUpdateExecuteListener implements ExecutionListener {

    private static final long serialVersionUID = 6500710286639731389L;

    @Override
    public void notify(DelegateExecution execution) {
        InvoiceApplicationBpmApi bpmApi = SpringUtil.getBean(InvoiceApplicationBpmApi.class);
        log.info("触发了收款申请再次提交任务，BusinessKey:{}", execution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(execution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
