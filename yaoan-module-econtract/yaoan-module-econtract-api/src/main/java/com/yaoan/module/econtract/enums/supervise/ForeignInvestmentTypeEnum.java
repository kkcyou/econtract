package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @author ZHC
 * 外商投资类型
 */

@Getter
public enum ForeignInvestmentTypeEnum {

    SOLE_FOREIGN_INVESTMENT(0, "外商单独投资"),
    PARTIAL_FOREIGN_INVESTMENT(1, "外商部分投资"),
    DOMESTIC_INVESTMENT(2, "内资");

    private final Integer code;
    private final String info;

    ForeignInvestmentTypeEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ForeignInvestmentTypeEnum getInstance(Integer code) {
        for (ForeignInvestmentTypeEnum value : ForeignInvestmentTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
