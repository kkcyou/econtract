package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-16 15:58
 */
@Slf4j
public class ConfirmRejectExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("触发了合同相对方确认拒签任务，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        contractApi.updateConfirmReject(delegateExecution.getProcessInstanceId());

    }
}