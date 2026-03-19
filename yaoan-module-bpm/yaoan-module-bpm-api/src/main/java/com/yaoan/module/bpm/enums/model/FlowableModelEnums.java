package com.yaoan.module.bpm.enums.model;

import lombok.Getter;

/**
 * @description: 工作流模型的相关枚举
 * @author: Pele
 * @date: 2024/2/2 10:32
 */
@Getter
public enum FlowableModelEnums {
    /**
     * 工作流模型的相关枚举
     */
    START(-1, "start", "申请人发起"),
    UPDATE(0, "update", "申请人更改"),
    FIRST_LEVEL(1, "first_level", "一级审批"),
    SECOND_LEVEL(2, "second_level", "二级审批"),
    THIRD_LEVEL(3, "third_level", "三级审批"),
    FORTH_LEVEL(4, "forth_level", "四级审批"),
    FIFTH_LEVEL(5, "fifth_level", "五级审批"),
    APPLICANT_UPDATE(6, "applicant_update", "第一岗申请人更改"),


    WITHDRAWAL(85, "withdrawal", "撤回任务"),

    /**
     * 双方合同签署审批节点
     */
    COMPANY_IDS_TAG(0, "companyIdsTag", "指定公司ID标识"),

    BOTH_CONFIRM_01(1, "confirm01", "相对方确认"),
    BOTH_CONFIRM_02(2, "confirm02", "发送方确认"),
    BOTH_SIGN_01(3, "sign01", "主体签署"),
    BOTH_SIGN_02(4, "sign02", "主体签署01"),

    BOTH_CONFIRM_02_SORT(202, "confirm02_sort", "发送方确认"),
    BOTH_SIGN_01_SORT(302, "sign01_sort", "主体签署"),



    /**
     * 三方合同签署审批节点
     */
    TRIPARTITE_CONFIRM_01(1, "confirmT01", "相对方确认T01"),
    TRIPARTITE_CONFIRM_02(2, "confirmT02", "相对方确认T02"),
    TRIPARTITE_CONFIRM_03(3, "confirmT03", "发送方确认T03"),
    TRIPARTITE_SIGN_01(4, "signT01", "主体签署T01"),
    TRIPARTITE_SIGN_02(5, "signT02", "主体签署T02"),
    TRIPARTITE_SIGN_03(6, "signT03", "主体签署T03"),


    ;

    /**
     * 序号 1 ~ 6是审批节点的顺序
     */
    private final Integer index;
    /**
     * 编号：对应 bpm_task_assign_rule 的 task_definition_key
     */
    private final String key;
    /**
     * 名称 bpm_task_ext 的 name
     */
    private final String name;


    FlowableModelEnums(Integer index, String key, String name) {
        this.index = index;
        this.key = key;
        this.name = name;
    }

    public static FlowableModelEnums getInstanceByKey(String key) {
        for (FlowableModelEnums value : FlowableModelEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }

    public static FlowableModelEnums getInstanceByName(String name) {
        for (FlowableModelEnums value : FlowableModelEnums.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 通过序号获得节点信息
     */
    public static FlowableModelEnums getInstanceByIndex(Integer index) {
        for (FlowableModelEnums value : FlowableModelEnums.values()) {
            if (value.getIndex().equals(index)) {
                return value;
            }
        }
        return null;
    }
}
