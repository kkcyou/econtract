package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum ParamTypeEnums {
    //参数类型：txt：文本，table：表格，drop-down：下拉框，date：日期
    TXT  ("txt", "文本"),
    TBLE  ("table", "表格"),
    DATE  ("date", "日期"),
    DORP_DOWN  ("drop-down", "下拉框");
    private final String code;
    private final String info;

    ParamTypeEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static ParamTypeEnums getInstance(String code) {
        for (ParamTypeEnums value : ParamTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
