package com.yaoan.module.bpm.service.listener.payment.invoice;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.econtract.api.bpm.payment.invoice.InvoiceApplicationBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/4 11:31
 */
@Component
public class BpmInvoiceResultListener extends BpmProcessInstanceStatusEventListener {
    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            InvoiceApplicationBpmApi api = SpringUtil.getBean(InvoiceApplicationBpmApi.class);
            api.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
