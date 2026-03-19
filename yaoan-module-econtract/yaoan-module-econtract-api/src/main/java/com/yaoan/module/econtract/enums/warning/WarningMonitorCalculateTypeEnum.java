package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningMonitorCalculateTypeEnum {
    // 超期
    NOW_SUB(0, "减监控字段"),
    // 临期
    SUB_NOW(1, "监控字段减去");

    private final Integer code;
    private final String desc;

    WarningMonitorCalculateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningMonitorCalculateTypeEnum getInstance(Integer code) {
        for (WarningMonitorCalculateTypeEnum value : WarningMonitorCalculateTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
