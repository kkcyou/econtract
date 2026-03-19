package com.yaoan.module.bpm.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-6-13 18:01
 */
@Getter
public enum WithdrawalFlagEnums {
    /**
     * 撤回业务类型 枚举类
     */
    CLOSING(0, "解除"),

    ;

    private final Integer code;
    private final String info;

    WithdrawalFlagEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static WithdrawalFlagEnums getInstance(Integer code) {
        for (WithdrawalFlagEnums value : WithdrawalFlagEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
