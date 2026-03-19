package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/4 12:19
 */
@Getter
public enum TemplateEnums {

    GOVERNMENT_TEMPLATE  ("1", "官方范本"),
    STANDARD_TEMPLATE  ("2", "地方标准文本"),
    THIRD_PARTY_TEMPLATE  ("3", "第三方合作文本");

    private final String code;
    private final String info;

    TemplateEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static TemplateEnums getInstance(String code) {
        for (TemplateEnums value : TemplateEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
