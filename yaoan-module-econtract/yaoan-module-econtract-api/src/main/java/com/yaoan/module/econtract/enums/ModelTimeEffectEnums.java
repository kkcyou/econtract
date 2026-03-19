package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/27 17:16
 */
@Getter
public enum ModelTimeEffectEnums {

    FOREVER (0, "部分时间有效"),
    LIMITED_TIME (1, "长期有效");


    private final Integer code;
    private final String info;

    ModelTimeEffectEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static ModelTimeEffectEnums getInstance(Integer code) {
        for (ModelTimeEffectEnums value : ModelTimeEffectEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
