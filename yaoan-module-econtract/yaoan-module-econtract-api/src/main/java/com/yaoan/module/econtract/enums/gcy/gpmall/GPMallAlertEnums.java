package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description: 电子卖场-待办提醒 枚举类
 * @author: Pele
 * @date: 2023/12/13 19:44
 */
@Getter
public enum GPMallAlertEnums {
    /**
     * 合同待办提醒 枚举类
     */
    ALERT_STAGE_TO_BE_SIGNED(2, "待签章", "签章阶段"),
    ALERT_STAGE_TO_BE_SENT(0, "待确认", "确认阶段");
    private final Integer code;
    private final String info;
    private final String stageName;

    GPMallAlertEnums(Integer code, String info, String stageName) {
        this.code = code;
        this.info = info;
        this.stageName = stageName;
    }

    public static GPMallAlertEnums getInstance(Integer code) {
        for (GPMallAlertEnums value : GPMallAlertEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
