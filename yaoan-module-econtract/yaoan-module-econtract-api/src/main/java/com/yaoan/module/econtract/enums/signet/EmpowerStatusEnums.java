package com.yaoan.module.econtract.enums.signet;

import lombok.Getter;


@Getter
public enum EmpowerStatusEnums {
    /**
     * 授权状态 枚举类
     */
    NORMAL(1, "正常"),
    LAPSE(0, "失效");

    private final Integer code;
    private final String desc;

    EmpowerStatusEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static EmpowerStatusEnums getInstance(Integer code) {
        for (EmpowerStatusEnums value : EmpowerStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getDesc(Integer code) {
        for (EmpowerStatusEnums value : EmpowerStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

}
