package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum CardTypeEnums {
    CREDIT(2, "统一社会信用代码"),
    IDCARD(1, "身份证");

    private final Integer code;
    private final String info;

    CardTypeEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static CardTypeEnums getInstance(Integer code) {
        for (CardTypeEnums value : CardTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
