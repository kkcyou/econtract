package com.yaoan.module.econtract.controller.admin.alert.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 20:32
 */
@Data
public class AlertRespVO {
    /**
     * 合同提醒主键
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 流程阶段
     */
    private String flowStage;

    /**
     * 流程阶段Str
     */
    private String flowStageStr;

    /**
     * 提醒内容
     */
    private String alertContent;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

}
