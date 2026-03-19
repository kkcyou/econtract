package com.yaoan.module.econtract.enums.signet;

import lombok.Getter;



@Getter
public enum SignetStatusEnums {
    /**
     * 印章状态 枚举类
     */
    DEACTIVATED(0, "已停用"),
    ENABLED(1, "已启用"),
    WRITE_OFF (2, "已注销"),
    EXPIRED(3, "已过期"),
    UNENABLED(4, "未启用");

    private final Integer code;
    private final String desc;

    SignetStatusEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static SignetStatusEnums getInstance(Integer code) {
        for (SignetStatusEnums value : SignetStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getDesc(Integer code) {
        for (SignetStatusEnums value : SignetStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

}
