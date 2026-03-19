package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description: 合同起草方 枚举类
 * @author: zhc
 * @date: 2023/11/28 16:27
 */
@Getter
public enum ContractDrafterEnums {

    /**
     * 合同起草方：采购人（1）/供应商（2）。默认为供应商
     */
    ORG_SEND(1, "采购人起草"),

    SUPPLIER_SEND(2, "供应商起草");

    private final Integer code;
    private final String info;

    ContractDrafterEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractDrafterEnums getInstance(Integer code) {
        for (ContractDrafterEnums value : ContractDrafterEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
