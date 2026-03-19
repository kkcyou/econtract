package com.yaoan.module.system.enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/18 11:34
 */
@Getter
@AllArgsConstructor
public enum SystemConfigTypeEnums {
    /**
     * 起草方配置类
     */
    CONFIG_DRAFT(101,"起草方配置")
    ;

    private final Integer key;
    private final String info;


    public static SystemConfigTypeEnums getInstance(Integer key) {
        for (SystemConfigTypeEnums value : SystemConfigTypeEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }

}
