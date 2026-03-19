package com.yaoan.module.econtract.enums.order;

import lombok.Getter;

@Getter
public enum GCYOrderStatusEnums {

    /**
     * 订单状态 枚举类
     */
    WAITE_TO_DRAFT("0", "待草拟"),

    DRAFTED("1", "已草拟");

    private final String code;
    private final String info;

    GCYOrderStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static GCYOrderStatusEnums getInstance(String code) {
        for (GCYOrderStatusEnums value : GCYOrderStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}