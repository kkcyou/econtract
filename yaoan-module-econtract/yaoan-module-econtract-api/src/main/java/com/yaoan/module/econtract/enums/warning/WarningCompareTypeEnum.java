package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningCompareTypeEnum {
    RUN_NOW(0, "立即执行", "now"),
    LESS(1, "小于", "<"),
    LESS_EQUAL(2, "小于等于", "<="),
    EQUAL(3, "等于", "="),
    GREATER_EQUAL(4, "大于等于", ">="),
    GREATER(5, "大于", ">"),
    BETWEEN(6, "范围", "between"),
    NOT_EQUAL(7, "不等于", "<>"),
    IN(8, "在其中", "in");


    private final Integer code;
    private final String desc;
    private final String mathSymbol;

    WarningCompareTypeEnum(Integer code, String desc, String mathSymbol) {
        this.code = code;
        this.desc = desc;
        this.mathSymbol = mathSymbol;
    }
    public static WarningCompareTypeEnum getInstance(Integer code) {
        for (WarningCompareTypeEnum value : WarningCompareTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
