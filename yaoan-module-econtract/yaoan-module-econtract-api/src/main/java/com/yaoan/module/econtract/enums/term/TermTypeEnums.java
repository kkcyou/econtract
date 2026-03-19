package com.yaoan.module.econtract.enums.term;

import lombok.Getter;

/**
 * @description: 条款类型 枚举类
 * @author: Pele
 * @date: 2024/1/11 18:45
 */
@Getter
public enum TermTypeEnums {
    /**
     * 条款类型 枚举类
     */
    HEAD("head",  "合同封顶"),
    COM("com",  "合同条款"),
    END("end",  "合同结尾"),
    ;


    private final String code;
    private final String info;

    TermTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static TermTypeEnums getInstance(String code) {
        for (TermTypeEnums value : TermTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
