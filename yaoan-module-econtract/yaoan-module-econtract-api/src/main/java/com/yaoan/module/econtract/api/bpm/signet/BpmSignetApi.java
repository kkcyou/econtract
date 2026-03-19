package com.yaoan.module.econtract.api.bpm.signet;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @author doujiale
 */
public interface BpmSignetApi {

    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     * @param statusEnums 审批状态枚举
     */
    void notifyBpmSignetApproveStatus( String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
