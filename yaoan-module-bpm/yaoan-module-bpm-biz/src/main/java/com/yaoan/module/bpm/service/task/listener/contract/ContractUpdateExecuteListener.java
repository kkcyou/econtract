package com.yaoan.module.bpm.service.task.listener.contract;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.ContractFlowNodeTypeEnums;
import com.yaoan.module.econtract.api.bpm.contract.BpmContractApi;
import com.yaoan.module.econtract.api.bpm.payment.PaymentApplicationBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description: 合同申请更改执行监听器
 * @author: Pele
 * @date: 2024/1/15 10:59
 */
@Slf4j
public class ContractUpdateExecuteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        BpmContractApi paymentApi = SpringUtil.getBean(BpmContractApi.class);
        log.info("触发了合同申请重新修改任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        paymentApi.notifyBpmContactApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
