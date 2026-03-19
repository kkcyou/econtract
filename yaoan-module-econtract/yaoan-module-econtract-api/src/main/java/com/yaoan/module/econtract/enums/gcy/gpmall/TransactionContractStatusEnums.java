package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 项目采购的合同状态的枚举类
 *
 * @author Tdb
 * @version 1.0
 * @date 2024-01-24 11:41
 */
@Getter
public enum TransactionContractStatusEnums {
    PENDING_DRAFT( "0","待草拟"),
    SIGNING( "1","签订中"),
    SIGNED( "9","已签订"),
    INVALID( "4","已作废");


    private final String code;
    private final String info;

    TransactionContractStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static TransactionContractStatusEnums getInstance(String code) {
        for (TransactionContractStatusEnums value : TransactionContractStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
