package com.yaoan.module.bpm.service.listener.contract;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.econtract.api.bpm.contract.BpmContractApi;
import org.springframework.stereotype.Component;

import static com.yaoan.module.econtract.enums.ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE;

/**
 * 合同审批的结果的监听器实现类
 *
 * @author doujl
 */
@Component
public class BpmContractResultListener extends BpmProcessInstanceStatusEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        return CONTRACT_DRAFT_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            BpmContractApi bpmContractApi = SpringUtil.getBean(BpmContractApi.class);
            bpmContractApi.notifyBpmContactApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
        }
    }
}
