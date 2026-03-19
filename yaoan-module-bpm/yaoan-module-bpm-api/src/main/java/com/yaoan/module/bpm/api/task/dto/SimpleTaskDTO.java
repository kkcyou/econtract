package com.yaoan.module.bpm.api.task.dto;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/1 18:49
 */
@Data
public class SimpleTaskDTO {
    /**
     * 编号，自增
     */
    private Long id;

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

    /**
     * 任务的结果
     *
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 任务的结束时间
     *
     * 冗余 HistoricTaskInstance 的 endTime  属性
     */
    private LocalDateTime endTime;

    /**
     * 流程实例的编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;

    /**
     * 抄送人ids
     */
    private Set<Long> copyRecipientIds;

    private String processDefinitionKey;
}
