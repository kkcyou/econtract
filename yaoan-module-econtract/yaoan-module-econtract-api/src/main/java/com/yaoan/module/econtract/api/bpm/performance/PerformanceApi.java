package com.yaoan.module.econtract.api.bpm.performance;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @author doujiale
 */
public interface PerformanceApi {

    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyPerformanceApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
