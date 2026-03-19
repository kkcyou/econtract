package com.yaoan.module.econtract.api.bpm.contractBorrow;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/9 16:12
 */
public interface ContractBorrowBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) throws Exception;

}
