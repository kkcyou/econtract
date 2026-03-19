package com.yaoan.module.econtract.api.bpm.payment;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/26 15:59
 */
public interface PaymentApplicationBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
