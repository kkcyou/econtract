package com.yaoan.module.econtract.api.bpm.model;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @author Pele
 */
public interface ModelBpmApi {

    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
