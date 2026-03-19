package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class BpmTaskRespDTO  {
    /**
     * 任务的名字
     *
     * 冗余 Task 的 name 属性，为了筛选
     */
    private String name;
    /**
     * 任务的编号
     *
     * 关联 Task 的 id 属性
     */
    private String taskId;

    private String definitionKey;
    private LocalDateTime endTime;
    private Integer result;
    private String reason;
    private String assigneeUserName;
    private String assignee;
    private Date claimTime;

    private String processInstanceId;

}
