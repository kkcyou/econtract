package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.system.api.config.SystemConfigApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 合同发送方签署任务的监听器
 *
 * @author doujiale
 */
@Slf4j
public class SignExecuteListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("触发了合同发送方签署任务，ProcessInstanceId:{}", delegateExecution.getProcessInstanceId());
        contractApi.updateSign(delegateExecution.getProcessInstanceId());

    }
}
