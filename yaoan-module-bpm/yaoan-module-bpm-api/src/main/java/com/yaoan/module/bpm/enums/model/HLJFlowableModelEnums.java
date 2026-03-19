package com.yaoan.module.bpm.enums.model;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/13 16:16
 */
@Getter
public enum HLJFlowableModelEnums {

    BOTH_CONFIRM_01(1, "confirm01", "relativeId", "相对方确认"),
    BOTH_CONFIRM_02(2, "confirm02", "sendId", "发送方确认"),
    BOTH_SIGN_01(3, "sign01", "supplierId", "主体签署"),
    BOTH_SIGN_02(4, "sign02", "buyerOrgId", "主体签署01"),
    ORG_CONFIRM(7, "org_confirm", "buyerOrgId", "采购人确认"),
    SUPPLIER_CONFIRM(8, "supplier_confirm", "supplierId", "供应商确认"),
    SUPPLIER_SIGN(9, "supplier_sign", "supplierId", "供应商盖章"),
    ORG_SIGN(10, "org_sign", "buyerOrgId", "采购人盖章"),
    SUPPLIER_DRAFT(11, "supplier_draft", "supplierId", "供应商草拟合同"),
    ORG_SIGN_02(5, "org_sign", "buyerOrgId", "采购人盖章"),
    SUPPLIER_SIGN_02(6, "supplier_sign", "supplierId", "供应商盖章"),
    ORG_DRAFT(12, "org_draft", "buyerOrgId", "采购人草拟合同"),
    AGENCY(13, "agency", "buyerOrgId", "代理机构审批"),
    ;
    /**
     * 序号 1 ~ 5 是审批节点的顺序
     */
    private final Integer index;
    /**
     * 编号：对应 bpm_task_assign_rule 的 task_definition_key
     */
    private final String key;
    private final String param;
    /**
     * 名称 bpm_task_ext 的 name
     */
    private final String name;

    HLJFlowableModelEnums(Integer index, String key, String param, String name) {
        this.index = index;
        this.key = key;
        this.param = param;
        this.name = name;
    }

    public static HLJFlowableModelEnums getInstanceByKey(String key) {
        for (HLJFlowableModelEnums value : HLJFlowableModelEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }

    public static HLJFlowableModelEnums getInstanceByName(String name) {
        for (HLJFlowableModelEnums value : HLJFlowableModelEnums.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 通过序号获得节点信息
     */
    public static HLJFlowableModelEnums getInstanceByIndex(Integer index) {
        for (HLJFlowableModelEnums value : HLJFlowableModelEnums.values()) {
            if (value.getIndex().equals(index)) {
                return value;
            }
        }
        return null;
    }
}
