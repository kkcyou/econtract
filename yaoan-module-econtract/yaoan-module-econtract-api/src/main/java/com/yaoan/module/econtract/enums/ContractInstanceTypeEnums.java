package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @author doujiale
 */
@Getter
public enum ContractInstanceTypeEnums {

    CONTRACT_CONFIRM(0, "合同确认"),
    CONTRACT_SIGN(1, "合同签署");


    private final Integer code;
    private final String desc;

    ContractInstanceTypeEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static ContractInstanceTypeEnums getInstance(Integer code) {
        for (ContractInstanceTypeEnums value : ContractInstanceTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
