package com.yaoan.module.econtract.api.bpm.company;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 11:03
 */
public interface BpmCompanyApi {


    List<Long> calculateUsers4SaasCompanyExpression(String bpmId);

    void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum instance);
}
