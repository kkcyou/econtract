package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.system.api.config.SystemConfigApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 合同确认任务的监听器
 *
 * @author doujiale
 */
@Slf4j
public class ConfirmExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("触发了合同确认任务，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        //判断是否走用印审批
        Boolean needSignet = contractApi.isNeedSignet(delegateExecution.getProcessInstanceId(),null);
        if(needSignet){
            contractApi.notifyContractStatus(delegateExecution.getProcessInstanceId(), ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED);
        } else {
            contractApi.notifyContractStatus(delegateExecution.getProcessInstanceId(), ContractStatusEnums.TO_BE_SIGNED);
        }

    }
}
