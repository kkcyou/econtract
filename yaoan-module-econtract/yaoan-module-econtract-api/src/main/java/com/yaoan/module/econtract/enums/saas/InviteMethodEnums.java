package com.yaoan.module.econtract.enums.saas;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-31 17:06
 */
@Getter
public enum InviteMethodEnums {
    /**
     * 邀请方式 枚举类
     */
    STAFF_SELF(0, "员工自主加入"),

    ;

    private final Integer code;
    private final String info;

    InviteMethodEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static InviteMethodEnums getInstance(Integer code) {
        for (InviteMethodEnums value : InviteMethodEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
