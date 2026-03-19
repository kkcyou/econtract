package com.yaoan.module.econtract.enums.term;

import lombok.Getter;

/**
 * @description: 条款类型 枚举类
 * @author: Pele
 * @date: 2024/1/11 18:45
 */
@Getter
public enum TermLibraryEnums {
    /**
     * 条款类型 枚举类
     */
    COMMON(0,  "公共条款库"),
    AGENCY(1,  "单位条款库"),
    OTHERS(2,  "其他条款"),
    ;


    private final Integer code;
    private final String info;

    TermLibraryEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static TermLibraryEnums getInstance(Integer code) {
        for (TermLibraryEnums value : TermLibraryEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
