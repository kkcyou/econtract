package com.yaoan.module.bpm.service.task.listener.change.fast;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.change.FastContractChangeBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @author Pele
 */
@Slf4j
public class FastContractChangeUpdateExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = 805527037524587688L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        FastContractChangeBpmApi bpmApi = SpringUtil.getBean(FastContractChangeBpmApi.class);
        log.info("触发了合同变更重新修改任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.PROCESS);
    }
}
