package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

/**
 * @description: 通知对象枚举
 * @author: Pele
 * @date: 2025-6-25 18:19
 */
@Getter
public enum WarningNoticeFlowUserTypeEnums {
    CREATOR(0, "创建人"),
    ASSIGNEE(1, "节点当前办理人"),

    ;


    private final Integer code;
    private final String desc;

    WarningNoticeFlowUserTypeEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningNoticeFlowUserTypeEnums getInstance(Integer code) {
        for (WarningNoticeFlowUserTypeEnums value : WarningNoticeFlowUserTypeEnums.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
