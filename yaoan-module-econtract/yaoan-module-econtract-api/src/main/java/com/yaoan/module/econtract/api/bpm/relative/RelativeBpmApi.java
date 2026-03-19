package com.yaoan.module.econtract.api.bpm.relative;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/12 14:19
 */
public interface RelativeBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
