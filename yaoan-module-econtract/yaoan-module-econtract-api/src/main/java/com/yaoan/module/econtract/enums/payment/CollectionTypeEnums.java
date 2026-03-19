package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

@Getter
public enum CollectionTypeEnums {
    PAYMENT(0, "付款"),
    COLLECTION(1, "收款"),

    ;

    private final Integer code;
    private final String info;

    CollectionTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CollectionTypeEnums getInstance(Integer code) {
        for (CollectionTypeEnums value : CollectionTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
