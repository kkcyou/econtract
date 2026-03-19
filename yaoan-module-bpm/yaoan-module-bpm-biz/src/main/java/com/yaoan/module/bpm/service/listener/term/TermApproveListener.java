package com.yaoan.module.bpm.service.listener.term;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.payment.PaymentApplicationBpmApi;
import com.yaoan.module.econtract.api.bpm.term.TermBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/10 11:12
 */
@Component
public class TermApproveListener extends BpmProcessInstanceStatusEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            TermBpmApi termBpmApi = SpringUtil.getBean(TermBpmApi.class);
            termBpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }

}
