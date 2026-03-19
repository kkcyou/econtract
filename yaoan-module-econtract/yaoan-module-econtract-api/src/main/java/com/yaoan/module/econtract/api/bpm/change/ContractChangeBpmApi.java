package com.yaoan.module.econtract.api.bpm.change;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 18:11
 */
public interface ContractChangeBpmApi {
    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums);

    /**
     * 草稿箱提交监听
     * */
    void submit(String processInstanceBusinessKey, BpmProcessInstanceResultEnum bpmProcessInstanceResultEnum);
}
