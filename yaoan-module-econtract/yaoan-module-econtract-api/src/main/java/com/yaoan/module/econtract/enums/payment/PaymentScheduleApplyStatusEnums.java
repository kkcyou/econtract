package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description: 支付计划状态 枚举类
 * @author: Pele
 * @date: 2023/12/22 13:55
 */
@Getter
public enum PaymentScheduleApplyStatusEnums {
    /**
     * 支付计划状态 枚举类
     */
    NO_APPLY(0, "未申请"),
    APPLY(1, "已申请"),
    DRAFT(2, "草稿箱"),
    APPLY_SUCCESS(3, "申请通过");

    private final Integer code;
    private final String info;

    PaymentScheduleApplyStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PaymentScheduleApplyStatusEnums getInstance(Integer code) {
        for (PaymentScheduleApplyStatusEnums value : PaymentScheduleApplyStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
