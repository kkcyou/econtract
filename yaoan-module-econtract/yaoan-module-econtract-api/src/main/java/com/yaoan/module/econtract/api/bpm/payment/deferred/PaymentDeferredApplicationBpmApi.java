package com.yaoan.module.econtract.api.bpm.payment.deferred;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 17:42
 */
public interface PaymentDeferredApplicationBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);
}
