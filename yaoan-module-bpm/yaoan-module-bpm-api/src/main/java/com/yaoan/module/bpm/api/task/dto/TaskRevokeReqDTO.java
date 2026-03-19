package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;

/**
 * 任务撤回实例信息
 *
 * @author doujl
 */
@Data
public class TaskRevokeReqDTO {

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 代办任务ID
     */
    private String taskId;

}
