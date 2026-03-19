package com.yaoan.module.econtract.controller.admin.amount.vo.alert;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GPMallAlertListRespVO {
    /**
     * 合同或订单id
     */
    private String id;

    /**
     * 合同或订单名称
     */
    private String name;

    /**
     * 合同或订单更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 提醒内容
     */
    private String content;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 合同的订单id
     */
    private String contractOrderId;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 合同状态
     */
    private Integer status;
}