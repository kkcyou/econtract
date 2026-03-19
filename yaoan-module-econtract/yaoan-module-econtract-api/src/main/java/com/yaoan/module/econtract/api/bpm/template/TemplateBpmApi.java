package com.yaoan.module.econtract.api.bpm.template;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

import java.io.IOException;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 18:17
 */
public interface TemplateBpmApi {
    /**
     * 通知审批状态执行
     * @param businessKey 业务ID
     */
    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) throws Exception;

}
