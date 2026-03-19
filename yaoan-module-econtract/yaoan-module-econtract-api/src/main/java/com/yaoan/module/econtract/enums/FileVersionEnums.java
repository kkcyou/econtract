package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description: 文件版本枚举
 * @author: Pele
 * @date: 2024/8/29 15:37
 */
@Getter
public enum FileVersionEnums {

    /**
     * 文件版本枚举
     */
    CONTRACT(1, "合同"),
    MODEL(2, "模板"),

    ;
    private final Integer code;
    private final String info;

    FileVersionEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static FileVersionEnums getInstance(Integer code) {
        for (FileVersionEnums value : FileVersionEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
