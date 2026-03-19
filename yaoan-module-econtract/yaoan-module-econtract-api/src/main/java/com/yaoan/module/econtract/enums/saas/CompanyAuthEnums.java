package com.yaoan.module.econtract.enums.saas;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 15:48
 */
@Getter
public enum CompanyAuthEnums {
    /**
     * 企业认证情况 枚举类
     */
    TODO(0, "未认证"),

    DONE(1, "已认证"),
    ;

    private final Integer code;
    private final String info;

    CompanyAuthEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CompanyAuthEnums getInstance(Integer code) {
        for (CompanyAuthEnums value : CompanyAuthEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
