package com.yaoan.module.econtract.enums.contractreviewitems;

import lombok.Getter;


@Getter
public enum BasisTypeEnums {
    /**
     * 规则依据类型
     */
    LEGAL_BASIS(1, "法律依据"),
    LAWYER_ADVICE(2, "律师建议"),
    LOGIC(3, "基本逻辑"),

    ;




    private final Integer code;
    private final String info;

    BasisTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BasisTypeEnums getInstance(Integer code) {
        for (BasisTypeEnums value : BasisTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
