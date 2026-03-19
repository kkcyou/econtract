package com.yaoan.module.econtract.enums.contract;

import lombok.Getter;

/**
 * @description:
 *  合同类别枚举（广西大学招标要求）主合同、补充协议、框架协议
 * @author: Pele
 * @date: 2025-10-14 16:18
 */
@Getter
public enum ContractKindEnums {
    /**
     * 合同类别枚举
     */
    MAIN_CONTRACT(0, "主合同"),

    SUPPLEMENTARY_AGREEMENT(1, "补充协议"),

    FRAMEWORK_AGREEMENT(2, "框架协议"),

    ;

    private final Integer code;
    private final String info;

    ContractKindEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractKindEnums getInstance(Integer code) {
        for (ContractKindEnums value : ContractKindEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
