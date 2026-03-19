package com.yaoan.module.bpm.service.task.listener.contract;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.ContractFlowNodeTypeEnums;
import com.yaoan.module.econtract.api.bpm.contract.BpmContractApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 合同申请退回执行监听器
 * @author: Pele
 * @date: 2024/1/15 10:58
 */
@Slf4j
public class ContractBackExecuteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        BpmContractApi api = SpringUtil.getBean(BpmContractApi.class);
        log.info("触发了合同申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        api.notifyBpmContactApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}
