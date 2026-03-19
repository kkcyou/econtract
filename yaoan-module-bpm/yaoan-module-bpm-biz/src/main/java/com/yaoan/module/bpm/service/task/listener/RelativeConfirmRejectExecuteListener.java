package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 合同相对方撤回签署任务的监听器
 *
 * @author xhx
 */
@Slf4j
public class RelativeConfirmRejectExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("触发了合同相对方拒签签署任务，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        contractApi.updateRelativeSignReject(delegateExecution.getProcessInstanceId(), "CONFIRM");

    }
}
