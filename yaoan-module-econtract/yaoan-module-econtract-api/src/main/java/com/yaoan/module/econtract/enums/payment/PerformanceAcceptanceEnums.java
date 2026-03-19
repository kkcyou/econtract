package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

@Getter
public enum PerformanceAcceptanceEnums {

    UNDO(0, "待验收"),
    DO(1, "验收通过"),
    REJECT(2,"验收不通过")
    ;

    private final Integer code;
    private final String info;

    PerformanceAcceptanceEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PerformanceAcceptanceEnums getInstance(Integer code) {
        for (PerformanceAcceptanceEnums value : PerformanceAcceptanceEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
