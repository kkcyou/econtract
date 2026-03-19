package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningMonitorCompareTypeEnum {
    BY_VALUE(0, "取值"),
    BY_CALCULATION(1, "计算"),
    BY_SQL(2, "sql查询取值"),
    BY_CAL_SQL(3, "根据sql计算"),
    ;


    private final Integer code;
    private final String desc;

    WarningMonitorCompareTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningMonitorCompareTypeEnum getInstance(Integer code) {
        for (WarningMonitorCompareTypeEnum value : WarningMonitorCompareTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
