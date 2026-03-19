package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description: 付款类型 枚举类
 * @author: Pele
 * @date: 2023/12/22 10:26
 */
@Getter
public enum PaymentTypeEnums {
    /**
     * 付款类型 枚举类
     */
//    NO_TICKET(0, "无票支付"),
//    TICKET(1, "有票支付"),
    FIRST_TICKET(0, "先款后票"),
    FIRST_CASH(1, "先票后款"),
    ;

    private final Integer code;
    private final String info;

    PaymentTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PaymentTypeEnums getInstance(Integer code) {
        for (PaymentTypeEnums value : PaymentTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
