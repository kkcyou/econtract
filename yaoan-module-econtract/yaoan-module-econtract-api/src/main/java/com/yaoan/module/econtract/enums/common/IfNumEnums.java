package com.yaoan.module.econtract.enums.common;

import lombok.Getter;

/**
 * @description:
 * @author: xhx
 * @date: 2024/3/1 14:24
 */
@Getter
public enum IfNumEnums {

    /**
     * 是否 的枚举
     */
    RJ(-1, "删除/拒绝/其他/特殊"),
    YES(1, "是"),

    NO(0, "否"),
    All(2,"全部"),


    ;

    private final Integer code;
    private final String info;

    IfNumEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static IfNumEnums getInstance(Integer code) {
        for (IfNumEnums value : IfNumEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
