package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @description:采购组织形式枚举
 * @author: zhc
 * @date: 2024-03-18
 */
@Getter
public enum KindEnums {
    /**
     * 实施形式枚举类
     */
    GOVE_PURCHASING("1", "政府集中采购"),
    DEPT_PURCHASING("2", "部门集中采购"),
    DISPERSE_PURCHASING("3", "分散采购"),
    ONESELF_PURCHASING("4", "自行采购");

    private final String code;
    private final String info;

    KindEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static KindEnums getInstance(String code) {
        for (KindEnums value : KindEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
