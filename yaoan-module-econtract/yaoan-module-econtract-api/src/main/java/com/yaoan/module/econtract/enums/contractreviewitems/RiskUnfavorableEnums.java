package com.yaoan.module.econtract.enums.contractreviewitems;

import lombok.Getter;


@Getter
public enum RiskUnfavorableEnums {
    /**
     * 风险不利方枚举
     */
    PARTY_A(1, "甲方"),
    PARTY_B(2, "乙方"),
    GENERAL(3, "通用")
    ;




    private final Integer code;
    private final String info;

    RiskUnfavorableEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static RiskUnfavorableEnums getInstance(Integer code) {
        for (RiskUnfavorableEnums value : RiskUnfavorableEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
