package com.yaoan.module.bpm.enums.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 15:23
 */
@Getter
@AllArgsConstructor
public enum ContractFlowNodeTypeEnums {
    /**
     * 合同工作流节点类型 枚举
     */
    TASK_NODE(0, "任务节点", ""),
    RESULT_NODE(1, "结束节点", ""),
    ;

    /**
     * 操作类型
     */
    private final Integer type;
    /**
     * 操作名字
     */
    private final String name;
    /**
     * 操作描述
     */
    private final String comment;
}
