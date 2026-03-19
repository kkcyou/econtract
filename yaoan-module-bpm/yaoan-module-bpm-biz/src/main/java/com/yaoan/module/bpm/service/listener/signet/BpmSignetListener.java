package com.yaoan.module.bpm.service.listener.signet;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.econtract.api.bpm.contract.BpmContractApi;
import com.yaoan.module.econtract.api.bpm.signet.BpmSignetApi;
import org.springframework.stereotype.Component;

import static com.yaoan.module.econtract.enums.ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE;

/**
 * 用印的结果的监听器实现类
 *
 * @author doujl
 */
@Component
public class BpmSignetListener extends BpmProcessInstanceStatusEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        return SEAL_APPLICATION_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            BpmSignetApi bpmSignetApi = SpringUtil.getBean(BpmSignetApi.class);
            bpmSignetApi.notifyBpmSignetApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
