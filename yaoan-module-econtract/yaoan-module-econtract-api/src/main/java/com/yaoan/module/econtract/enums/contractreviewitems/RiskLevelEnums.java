package com.yaoan.module.econtract.enums.contractreviewitems;

import lombok.Getter;


@Getter
public enum RiskLevelEnums {
    /**
     * 风险等级
     */
    // 低风险
    LOW_RISK(1, "低风险"),
    // 中风险
    MEDIUM_RISK(2, "中风险"),
    // 高风险
    HIGH_RISK(3, "高风险"),


    ;

    private final Integer code;
    private final String info;

    RiskLevelEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static RiskLevelEnums getInstance(Integer code) {
        for (RiskLevelEnums value : RiskLevelEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
