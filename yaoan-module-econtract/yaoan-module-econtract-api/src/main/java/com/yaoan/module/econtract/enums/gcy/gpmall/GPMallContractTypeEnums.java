package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description:内蒙电子卖场合同类型枚举
 * @author: zhc
 * @date: 2024-02-01 16:21
 */
@Getter
public enum GPMallContractTypeEnums {
    /**
     * 合同类型(1:普通合同 ,2结算单合同,3:月结合同)
     */
    GENERAL_CONTRACT(1, "普通合同"),

    SETTLEMENT_CONTRACT(2, "结算单合同"),

    MONTHLY_STATEMENT_CONTRACT(3, "月结合同");

    private final Integer code;
    private final String info;

    GPMallContractTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static GPMallContractTypeEnums getInstance(Integer code) {
        for (GPMallContractTypeEnums value : GPMallContractTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
