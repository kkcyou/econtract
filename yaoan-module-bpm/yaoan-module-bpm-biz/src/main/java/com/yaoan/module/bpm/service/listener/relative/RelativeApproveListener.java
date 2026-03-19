package com.yaoan.module.bpm.service.listener.relative;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.econtract.api.bpm.relative.RelativeBpmApi;
import com.yaoan.module.econtract.api.bpm.term.TermBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/12 14:18
 */
@Component
public class RelativeApproveListener extends BpmProcessInstanceStatusEventListener{
    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            RelativeBpmApi bpmApi = SpringUtil.getBean(RelativeBpmApi.class);
            bpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
