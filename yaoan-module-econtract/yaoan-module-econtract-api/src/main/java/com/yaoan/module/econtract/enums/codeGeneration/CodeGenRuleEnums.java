package com.yaoan.module.econtract.enums.codeGeneration;

import lombok.Getter;

/**
 * @description: 编号生成规则枚举
 * @author: Pele
 * @date: 2024/9/9 10:19
 */
@Getter
public enum CodeGenRuleEnums {
    /**
     * 编号生成规则 的枚举
     */
    YEAR("year", "年"),

    MONTH("month", "月"),

    DAY("day", "天"),

    SORT("sort", "序号"),

    DIY("DIY_", "自定义"),

    ;

    private final String code;
    private final String info;

    CodeGenRuleEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CodeGenRuleEnums getInstance(String code) {
        for (CodeGenRuleEnums value : CodeGenRuleEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
