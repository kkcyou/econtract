package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/23 20:30
 */
@Getter
public enum PerformanceTypeEnums {
    /**
     * 履约类型 枚举类
     */
    PAYMENT(0, "付款"),
    RECEIVABLE(1, "收款"),

    ;

    private final Integer code;
    private final String info;

    PerformanceTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PerformanceTypeEnums getInstance(Integer code) {
        for (PerformanceTypeEnums value : PerformanceTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
