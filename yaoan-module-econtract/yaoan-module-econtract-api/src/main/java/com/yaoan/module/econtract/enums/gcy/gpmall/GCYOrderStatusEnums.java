package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/12 18:15
 */
@Getter
public enum GCYOrderStatusEnums {

    /**
     * 订单状态 枚举类
     */
    WAITE_TO_DRAFT("0", "待草拟"),
    CANCELED("-1", "已取消"),
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
