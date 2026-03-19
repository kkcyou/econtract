package com.yaoan.module.econtract.enums.common.flow;

import lombok.Getter;

/**
 * @description: 工作流状态枚举
 * @author: Pele
 * @date: 2024/2/29 17:06
 */
@Getter
public enum FlowableStatusEnums {
    /**
     * 订单状态 枚举类
     */
    TO_DO("TO_DO", "待审批"),

    DONE("DONE", "已审批"),

    ;

    private final String code;
    private final String info;

    FlowableStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static FlowableStatusEnums getInstance(String code) {
        for (FlowableStatusEnums value : FlowableStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
