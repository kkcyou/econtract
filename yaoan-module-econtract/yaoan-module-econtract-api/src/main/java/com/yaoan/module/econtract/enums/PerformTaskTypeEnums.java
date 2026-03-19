package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/5 21:33
 */
@Getter
public enum PerformTaskTypeEnums {
    /**
     * 履约任务类型枚举
     */
    PERFORM_TASK_TYPE_PAYMENT(1, "支付"),
    PERFORM_TASK_TYPE_ACCEPTANCE(2, "验收"),
    PERFORM_TASK_TYPE_HANDOVER(3, "交付"),
    PERFORM_TASK_TYPE_BILLING(4, "开票"),
    PERFORM_TASK_TYPE_OTHERS(5, "其他");


    private final Integer code;
    private final String info;

    PerformTaskTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PerformTaskTypeEnums getInstance(Integer code) {
        for (PerformTaskTypeEnums value : PerformTaskTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
