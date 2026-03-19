package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 待确认合同状态任务的监听器
 *
 * @author doujiale
 */
@Slf4j
public class ToBeConfirmExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {

        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("触发了合同待确认任务，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        contractApi.notifyContractStatus(delegateExecution.getProcessInstanceId(), ContractStatusEnums.TO_BE_CONFIRMED);
    }
}
