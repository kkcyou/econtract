package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
@Slf4j
public class TerminateStartExecuteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("发起终止合同签署流程，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        contractApi.notifyContractStatus(delegateExecution.getProcessInstanceId(), ContractStatusEnums.TERMINATE_SIGNIND);
    }
}
