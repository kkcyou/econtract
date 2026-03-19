package com.yaoan.module.bpm.service.listener.register;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.register.ContractRegisterBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 11:23
 */
@Component
public class ContractRegisterBpmSubmitApproveResultListener extends BpmProcessInstanceStatusEventListener {
    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            ContractRegisterBpmApi bpmApi = SpringUtil.getBean(ContractRegisterBpmApi.class);
            bpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
