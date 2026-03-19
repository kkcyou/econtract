package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum RelativeTypeEnums {
    Supplier(0, "供应商"),
    Custom(1, "客户");

    private final Integer code;
    private final String info;

    RelativeTypeEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static RelativeTypeEnums getInstance(Integer code) {
        for (RelativeTypeEnums value : RelativeTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
