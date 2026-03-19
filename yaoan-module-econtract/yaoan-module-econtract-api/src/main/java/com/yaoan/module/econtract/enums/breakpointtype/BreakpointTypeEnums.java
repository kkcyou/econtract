package com.yaoan.module.econtract.enums.breakpointtype;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-16 14:31
 */
@Getter
public enum BreakpointTypeEnums {

    /**
     * 模板相关 2001000 ~ 2001099
     */
    DISK("disk", "磁盘"),
    MINIO("minio", "minio");


    private final String code;
    private final String info;

    BreakpointTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BreakpointTypeEnums getInstance(String code) {
        for (BreakpointTypeEnums value : BreakpointTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
