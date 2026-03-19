package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum WarningRuleEnums {
    XDFZXFX  ("XDFZXFX", "相对方资信风险"),
    HTQSFX  ("HTQSFX", "合同签署风险"),
    HTFKFX  ("HTFKFX", "合同付款风险"),
    HTSKFX  ("HTSKFX", "合同收款风险"),
    HTGDFX  ("HTGDFX", "合同归档风险"),
    JYGHFX  ("JYGHFX", "借阅归还风险");

    private final String code;
    private final String info;

    WarningRuleEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static WarningRuleEnums getInstance(String code) {
        for (WarningRuleEnums value : WarningRuleEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
