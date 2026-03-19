package com.yaoan.module.econtract.enums.company;

import com.yaoan.module.econtract.enums.common.IfEnums;
import lombok.Data;
import lombok.Getter;

@Getter
public enum CompanyEnums {
    /**
     * 公司逻辑 的枚举
     */
    SUBMITTER("0", "发起方的默认向对方id"),

    NO("n", "不可以"),


    ;

    private final String code;
    private final String info;

    CompanyEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CompanyEnums getInstance(String code) {
        for (CompanyEnums value : CompanyEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
