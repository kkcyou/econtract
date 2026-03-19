package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum EntityTypeEnums {
    INDIVIDUAL  ("1", "个人"),
    COMPANY  ("2", "企业"),
    ORGANIZATION  ("3", "单位");


    private final String code;
    private final String info;

    EntityTypeEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static EntityTypeEnums getInstance(String code) {
        for (EntityTypeEnums value : EntityTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
