package com.yaoan.module.bpm.service.listener.archive;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.archive.ArchiveBpmApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import org.springframework.stereotype.Component;

/**
 * 归档申请审批
 */
@Component
public class BpmContractArchiveListener extends BpmProcessInstanceStatusEventListener {
    @Override
    protected String getProcessDefinitionKey() {
        return ActivityConfigurationEnum.CONTRACT_ARCHIVE_APPLY.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            ArchiveBpmApi bean = SpringUtil.getBean(ArchiveBpmApi.class);
            try {
                bean.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
