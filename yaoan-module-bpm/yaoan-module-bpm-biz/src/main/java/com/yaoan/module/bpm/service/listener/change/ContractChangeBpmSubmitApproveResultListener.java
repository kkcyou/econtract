package com.yaoan.module.bpm.service.listener.change;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.change.ContractChangeBpmApi;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/25 16:56
 */
@Component
public class ContractChangeBpmSubmitApproveResultListener extends BpmProcessInstanceResultEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        /** 回归正常合同变动逻辑的监听 */
//TODO        return ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey();
        return null;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getResult())) {
            ContractChangeBpmApi bpmApi = SpringUtil.getBean(ContractChangeBpmApi.class);
            bpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getResult()));
        }
    }
}
