package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/20 14:11
 */
@Getter
public enum ModelEffectEnum {

    LIMITED_TIME_EFFECT (0, "部分时间有效"),
    FOREVER_TIME_EFFECT (1, "长期时间有效");


    private final Integer code;
    private final String info;

    ModelEffectEnum(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static ModelEffectEnum getInstance(Integer code) {
        for (ModelEffectEnum value : ModelEffectEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
