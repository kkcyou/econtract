package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 电子合同用户关联流程实例信息
 *
 * @author doujl
 */
@Data
public class ContractProcessInstanceRelationInfoRespDTO {

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 待办任务ID
     */
    private String taskId;
    private Date createTime;
    private String processInstanceCreator;

    /**
     * 处理结果 执行中 通过 退回
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    private Integer processResult;

    /**
     * 最近一次任务处理结果
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    private Integer lastResult;

    /**
     * 任务当前处理人
     */
    private String assignee;

    /**
     * 当前人最近一次审批的时间
     */
    private LocalDateTime lastestApproveTime;
}
