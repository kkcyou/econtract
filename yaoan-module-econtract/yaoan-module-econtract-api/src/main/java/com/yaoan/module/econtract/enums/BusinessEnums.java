package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description: 三方业务相关枚举
 * @author: Pele
 * @date: 2023/11/13 14:38
 */
@Getter
public enum BusinessEnums {
    /**
     * 虚拟系统的业务类型
     */
    PROJECT_PURCHASING(1, "项目采购"),
    FRAMEWORK_AGREEMENT(2, "框架采购协议"),
    ELECTRONICS_STORE(3, "电子卖场");

    private final Integer code;
    private final String info;

    BusinessEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BusinessEnums getInstance(Integer code) {
        for (BusinessEnums value : BusinessEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
