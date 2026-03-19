package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 15:00
 */
@Getter
public enum RiskAlertEnums {
    /**
     * 风险提示的枚举
     */
    ALERT_SOURCE_PERFORMANCE(1, "履约"),
    ALERT_SOURCE_QICHACHA(2, "相对方");


    private final Integer code;
    private final String info;

    RiskAlertEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static RiskAlertEnums getInstance(Integer code) {
        for (RiskAlertEnums value : RiskAlertEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
