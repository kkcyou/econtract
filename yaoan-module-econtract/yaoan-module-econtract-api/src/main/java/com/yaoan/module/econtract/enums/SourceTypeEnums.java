package com.yaoan.module.econtract.enums;

import lombok.Getter;
@Getter
public enum SourceTypeEnums {
    Add(1, "模块内新增"),
    Import(2, "批量导入");

    private final Integer code;
    private final String info;

    SourceTypeEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static SourceTypeEnums getInstance(Integer code) {
        for (SourceTypeEnums value : SourceTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
