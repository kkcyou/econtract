package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningNoticeWayEnum {
    WEB_MESSAGE(1, "站内信"),
    MESSAGE(2, "短信"),
    EMAIL(3, "邮件");


    private final Integer code;
    private final String desc;

    WarningNoticeWayEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningNoticeWayEnum getInstance(Integer code) {
        for (WarningNoticeWayEnum value : WarningNoticeWayEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
