package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 是否需要备案
 * @create 2021-07-12 17:24
 */
@Getter
@AllArgsConstructor
public enum IsBakEnum {

    /**
     * 平台
     */
    NO(0, "不需要备案"),

    /**
     * 集采馆
     */
    YES(1, "需要备案");

    private final Integer code;
    private final String value;
    public static IsBakEnum getInstance(Integer code) {
        for (IsBakEnum value : IsBakEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
