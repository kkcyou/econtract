package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum AccountStatus {

    /**
     * 模板相关 2001000 ~ 2001099
     */
    UNACTIVATED ("0", "未激活"),
    ACTIVATED ("1", "已激活");


    private final String code;
    private final String info;

    AccountStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static AccountStatus getInstance(String code) {
        for (AccountStatus value : AccountStatus.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
