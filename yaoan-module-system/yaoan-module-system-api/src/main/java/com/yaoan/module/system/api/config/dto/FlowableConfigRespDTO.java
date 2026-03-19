package com.yaoan.module.system.api.config.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/26 17:06
 */
@Data
public class FlowableConfigRespDTO {
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
