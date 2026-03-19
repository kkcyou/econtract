package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * 外资类型
 */

@Getter
public enum CountryTypeEnum {

    MAJOR_INDUSTRY("0", "欧资企业"),
    MEDIUM_ENTERPRISE("1", "美资企业"),
    SMALL_ENTERPRISE("2", "日资企业"),
    MICRO_ENTERPRISE("99", "其他");

    private final String code;
    private final String info;

    CountryTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CountryTypeEnum getInstance(String code) {
        for (CountryTypeEnum value : CountryTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static CountryTypeEnum getInstanceByName(String info) {
        for (CountryTypeEnum value : CountryTypeEnum.values()) {
            if (value.getInfo().equals(info)) {
                return value;
            }
        }
        return null;
    }
}
