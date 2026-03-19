package com.yaoan.module.econtract.enums.codeGeneration;

import lombok.Getter;

/**
 * @description: 编号生成规则
 * @author: Pele
 * @date: 2024/9/5 11:38
 */
@Getter
public enum CodeGenBusinessTypeEnums {
    /**
     * 编号生成规则 的枚举
     */
    CONTRACT_RULE("contract", "合同编号规则"),

    TEMPLATE_RULE("template", "范本编号规则"),

    MODEL_RULE("model", "范本编号规则"),
    ;

    private final String code;
    private final String info;

    CodeGenBusinessTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static CodeGenBusinessTypeEnums getInstance(String code) {
        for (CodeGenBusinessTypeEnums value : CodeGenBusinessTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}


