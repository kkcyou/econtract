package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;
/**
 * 通知对象 枚举
 * */
@Getter
public enum WarningNoticeUserTypeEnum {
    BY_CONFIG(2, "通过配置选择"),
    BY_FLOW(1, "根据工作流获取"),
    BY_DATA(3, "根据业务数据获取");


    private final Integer code;
    private final String desc;

    WarningNoticeUserTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningNoticeUserTypeEnum getInstance(Integer code) {
        for (WarningNoticeUserTypeEnum value : WarningNoticeUserTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
