package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * 任务撤回实例信息
 *
 * @author doujl
 */
@Data
public class TaskForWarningReqDTO {

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    private Date endDate;
    private Date startDate;
    private Long tenantId;

    /**
     * 代办任务ID
     */
    private String taskId;

}
