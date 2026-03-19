package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum WhetherEnum {

    NO("正常状态", 0, "0"), YES("反常状态", 1, "1");

    private final String name;
    private final int value;
    private final String stringValue;

    WhetherEnum(String name, int value, String stringValue) {
        this.name = name;
        this.value = value;
        this.stringValue = stringValue;
    }

    public static WhetherEnum getInstance(String stringValue) {
        for (WhetherEnum value : WhetherEnum.values()) {
            if (value.getStringValue().equals(stringValue)) {
                return value;
            }
        }
        return null;
    }
}
