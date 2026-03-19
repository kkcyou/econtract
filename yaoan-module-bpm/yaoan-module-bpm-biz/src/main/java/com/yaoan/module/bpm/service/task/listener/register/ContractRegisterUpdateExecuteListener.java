package com.yaoan.module.bpm.service.task.listener.register;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.change.ContractChangeBpmApi;
import com.yaoan.module.econtract.api.bpm.register.ContractRegisterBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 11:49
 */
@Slf4j
public class ContractRegisterUpdateExecuteListener implements ExecutionListener {

    private static final long serialVersionUID = -7939425677309556552L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractRegisterBpmApi bpmApi = SpringUtil.getBean(ContractRegisterBpmApi.class);
        log.info("触发了合同登记申请重新修改任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
