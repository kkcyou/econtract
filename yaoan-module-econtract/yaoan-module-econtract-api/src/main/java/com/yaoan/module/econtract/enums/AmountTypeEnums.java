package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description: 结算类型
 * @author: Pele
 * @date: 2024/1/8 18:01
 */
@Getter
public enum AmountTypeEnums {

    /**
     * 结算类型 枚举类
     */

    PAY(0, "付款"),
    RECEIPT(1, "收款"),
    NO_SETTLE(2, "不结算"),
    DIRECTION(3, "收支双向"),
    ;

    private final Integer code;
    private final String info;

    AmountTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static AmountTypeEnums getInstance(Integer code) {
        for (AmountTypeEnums value : AmountTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
