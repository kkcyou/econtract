package com.yaoan.module.econtract.controller.admin.common.vo.flowable;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/26 10:04
 */
@Data
public class FlowableConfigRespVO {
    /**
     * 是否走审批流
     */
    private String ifFlowable;

    /**
     * 是否批量提交
     */
    private String ifBatchSubmit;

    /**
     * 是否批量审批
     */
    private String ifBatchApprove;
}
