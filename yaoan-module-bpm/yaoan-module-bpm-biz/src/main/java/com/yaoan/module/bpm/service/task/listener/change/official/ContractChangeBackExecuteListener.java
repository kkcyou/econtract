package com.yaoan.module.bpm.service.task.listener.change.official;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.change.ContractChangeBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 18:10
 */
@Slf4j
public class ContractChangeBackExecuteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractChangeBpmApi bpmApi = SpringUtil.getBean(ContractChangeBpmApi.class);
        log.info("触发了合同变更申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
