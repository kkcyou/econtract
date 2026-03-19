package com.yaoan.module.econtract.api.bpm.change;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/29 16:41
 */
public interface FastContractChangeBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
