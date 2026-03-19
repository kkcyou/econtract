package com.yaoan.module.econtract.api.bpm.contract;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.ContractFlowNodeTypeEnums;

/**
 * @author doujiale
 */
public interface BpmContractApi {

    /**
     * 通知审批状态执行
     *
     * @param businessKey 业务ID
     * @param statusEnums 审批状态枚举
     */
    void notifyBpmContactApproveStatus( String businessKey, BpmProcessInstanceResultEnum statusEnums);

}
