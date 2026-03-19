package com.yaoan.module.bpm.service.task.listener.change.fast;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.change.FastContractChangeBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 18:10
 */
@Slf4j
public class FastContractChangeBackExecuteListener implements ExecutionListener {
    private static final long serialVersionUID = -6859748041080209487L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        FastContractChangeBpmApi bpmApi = SpringUtil.getBean(FastContractChangeBpmApi.class);
        log.info("触发了合同变更申请退回任务，BusinessKey:{}", delegateExecution.getProcessInstanceBusinessKey());
        bpmApi.notifyApproveStatus(delegateExecution.getProcessInstanceBusinessKey(), BpmProcessInstanceResultEnum.BACK);
    }
}