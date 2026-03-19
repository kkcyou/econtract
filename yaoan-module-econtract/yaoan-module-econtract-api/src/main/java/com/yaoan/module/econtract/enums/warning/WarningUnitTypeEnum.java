package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningUnitTypeEnum {
    DAY(1, "自然日"),
    WORK_DAY(2, "工作日"),
    PERCENT(3, "百分比"),
    AMOUNT(4, "金额"),
    NUMBER(5, "数量");


    private final Integer code;
    private final String desc;

    WarningUnitTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningUnitTypeEnum getInstance(Integer code) {
        for (WarningUnitTypeEnum value : WarningUnitTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
