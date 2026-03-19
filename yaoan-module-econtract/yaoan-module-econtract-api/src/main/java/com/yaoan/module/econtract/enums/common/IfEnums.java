package com.yaoan.module.econtract.enums.common;

import com.yaoan.module.econtract.enums.order.GCYOrderStatusEnums;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/1 14:24
 */
@Getter
public enum IfEnums {

    /**
     * 是否 的枚举
     */
    YES("y", "可以"),

    NO("n", "不可以"),



    ;

    private final String code;
    private final String info;

    IfEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static IfEnums getInstance(String code) {
        for (IfEnums value : IfEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
