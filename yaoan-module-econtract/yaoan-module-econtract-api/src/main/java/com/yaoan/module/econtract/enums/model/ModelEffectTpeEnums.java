package com.yaoan.module.econtract.enums.model;

import com.yaoan.module.econtract.enums.AmountTypeEnums;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/4 11:24
 */
@Getter
public enum ModelEffectTpeEnums {
    /**
     * 结算类型 枚举类
     */

    TIME_LIMITED(0, "有时效期"),
    FOREVER(1, "永久有效"),
    ;

    private final Integer code;
    private final String info;

    ModelEffectTpeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ModelEffectTpeEnums getInstance(Integer code) {
        for (ModelEffectTpeEnums value : ModelEffectTpeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
