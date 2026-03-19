package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description: 票款方式 先款后票0 先票后款1
 * @author: Pele
 * @date: 2025-5-26 16:47
 */
@Getter
public enum InvoiceAmountTypeEnums {
    LIMITED_TIME_EFFECT(0, "先款后票"),
    FOREVER_TIME_EFFECT(1, "先票后款");


    private final Integer code;
    private final String info;

    InvoiceAmountTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static InvoiceAmountTypeEnums getInstance(Integer code) {
        for (InvoiceAmountTypeEnums value : InvoiceAmountTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
