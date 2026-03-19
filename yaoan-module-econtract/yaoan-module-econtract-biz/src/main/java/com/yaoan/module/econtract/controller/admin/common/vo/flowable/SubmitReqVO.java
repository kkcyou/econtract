package com.yaoan.module.econtract.controller.admin.common.vo.flowable;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/20 13:58
 */
@Data
public class SubmitReqVO {

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 流程任务ID
     */
    private String taskId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 审批状态
     */
    private String result;
}
