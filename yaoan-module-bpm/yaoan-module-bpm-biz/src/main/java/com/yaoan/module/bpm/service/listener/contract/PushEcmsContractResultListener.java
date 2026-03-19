package com.yaoan.module.bpm.service.listener.contract;


import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.api.contract.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PushEcmsContractResultListener implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        log.info("合同审批通过发送审批结果到电子合同，ProcessInstanceId:{}", execution.getProcessInstanceId());
      //不使用流程监听，避免后续异常导致推送无法回滚
       // contractApi.productApproveSendEcms(execution.getProcessInstanceId(),null);
    }
}
