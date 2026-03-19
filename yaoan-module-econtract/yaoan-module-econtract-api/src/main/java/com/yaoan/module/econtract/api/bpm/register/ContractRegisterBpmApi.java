package com.yaoan.module.econtract.api.bpm.register;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 11:24
 */
public interface ContractRegisterBpmApi {


    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
