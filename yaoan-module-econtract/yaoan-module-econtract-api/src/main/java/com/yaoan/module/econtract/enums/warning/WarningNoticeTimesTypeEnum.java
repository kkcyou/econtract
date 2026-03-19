package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningNoticeTimesTypeEnum {
    ONCE(0, "通知一次"),
    EVETRYDAY(1, "每天通知"),
    DIY_DAY(2, "自定义天数");

    private final Integer code;
    private final String desc;

    WarningNoticeTimesTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningNoticeTimesTypeEnum getInstance(Integer code) {
        for (WarningNoticeTimesTypeEnum value : WarningNoticeTimesTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
