package com.yaoan.module.bpm.api.task.dto;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/20 23:03
 */
@Data
public class BpmTaskAllInfoRespVO {

    /**
     * 编号，自增
     */
    private Long id;


    /**
     * 任务的审批人
     * <p>
     * 冗余 Task 的 assignee 属性
     */
    private Long assigneeUserId;

    /**
     * 任务的名字
     * <p>
     * 冗余 Task 的 name 属性，为了筛选
     */
    private String name;

    /**
     * 任务的编号
     * <p>
     * 关联 Task 的 id 属性
     */
    private String taskId;
//    /**
//     * 任务的标识
//     *
//     * 关联 {@link Task#getTaskDefinitionKey()}
//     */
//    private String definitionKey;
    /**
     * 任务的结果
     * <p>
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 审批建议
     */
    private String reason;

    /**
     * 任务的结束时间
     * <p>
     * 冗余 HistoricTaskInstance 的 endTime  属性
     */
    private LocalDateTime endTime;

    /**
     * 流程实例的编号
     * <p>
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 流程定义的编号
     * <p>
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String creator;

    /**
     * 更新者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updater;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 抄送人ids
     */
    private Set<Long> copyRecipientIds;
}
