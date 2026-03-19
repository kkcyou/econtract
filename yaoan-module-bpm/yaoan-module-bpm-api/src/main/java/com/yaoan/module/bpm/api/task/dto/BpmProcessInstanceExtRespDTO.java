package com.yaoan.module.bpm.api.task.dto;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/20 18:43
 */
@Data
public class BpmProcessInstanceExtRespDTO {
    /**
     * 编号，自增
     */
    private Long id;

    /**
     * 发起流程的用户编号
     * <p>
     * 冗余 HistoricProcessInstance 的 startUserId 属性
     */
    private Long startUserId;

    /**
     * 流程实例的名字
     * <p>
     * 冗余 ProcessInstance 的 name 属性，用于筛选
     */
    private String name;

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
     * 流程分类
     * <p>
     * 冗余 ProcessDefinition 的 category 属性
     * 数据字典 bpm_model_category
     */
    private String category;

    /**
     * 流程实例的状态
     * <p>
     * 枚举 {@link BpmProcessInstanceStatusEnum}
     */
    private Integer status;

    /**
     * 流程实例的结果
     * <p>
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 结束时间
     * <p>
     * 冗余 HistoricProcessInstance 的 endTime 属性
     */
    private LocalDateTime endTime;

    /**
     * 提交的表单值
     */
    private Map<String, Object> formVariables;

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
}
