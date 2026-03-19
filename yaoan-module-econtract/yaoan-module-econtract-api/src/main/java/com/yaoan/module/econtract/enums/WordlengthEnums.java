package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum WordlengthEnums {
    FIFTY  ("1", "≤50字"),
    ONE_HUNDRED  ("2", "≤100字"),
    FIVE_HUNDRED  ("3", "≤500字"),
    ONE_THOUSAND  ("4", "≤1000字");

    private final String code;
    private final String info;

    WordlengthEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static WordlengthEnums getInstance(String code) {
        for (WordlengthEnums value : WordlengthEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
