package com.yaoan.module.econtract.api.bpm.archive;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

public interface ArchiveBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);
}
