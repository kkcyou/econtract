package com.yaoan.module.econtract.enums.perform.log;

import lombok.Getter;

/**
 * @description:  履约日志的模块名称枚举
 * @author: Pele
 * @date: 2024/9/27 14:54
 */
@Getter
public enum PerformanceLogModuleNameEnums {
    /**
     * 履约日志的模块名称枚举
     */
    PAY_APPLICATION ("pay_application", "付款申请"),
    PERFORMANCE ("performance", "履约");


    private final String code;
    private final String info;

    PerformanceLogModuleNameEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static PerformanceLogModuleNameEnums getInstance(String code) {
        for (PerformanceLogModuleNameEnums value : PerformanceLogModuleNameEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
