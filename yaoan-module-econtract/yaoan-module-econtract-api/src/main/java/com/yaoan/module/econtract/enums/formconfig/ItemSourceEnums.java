package com.yaoan.module.econtract.enums.formconfig;

import lombok.Getter;

/**
 * @description: 表单项来源 枚举类
 * @author: Pele
 * @date: 2024/3/19 17:32
 */
@Getter
public enum ItemSourceEnums {
    /**
     * 表单项来源 枚举类
     */
    HAND_SETTING("hand_setting", "手动设置"),

    PARAM("param", "参数池");

    private final String code;
    private final String info;

    ItemSourceEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ItemSourceEnums getInstance(String code) {
        for (ItemSourceEnums value : ItemSourceEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
