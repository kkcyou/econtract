package com.yaoan.module.bpm.service.listener.payment.deferred;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.payment.deferred.PaymentDeferredApplicationBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 17:46
 */
@Component
public class PaymentDeferredApplicationListener extends BpmProcessInstanceStatusEventListener {
    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            PaymentDeferredApplicationBpmApi bpmApi = SpringUtil.getBean(PaymentDeferredApplicationBpmApi.class);
            bpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
