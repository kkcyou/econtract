package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @author xhx
 */

@Getter
public enum ContractFilingsStatusEnums {
    /**
     * 合同备案状态 枚举类
     */
    FILINGS_WAIT(0, "待备案", "未推监管备案"),
    FILINGS_ING(1, "备案中", "已推监管备案中"),
    FILINGS_DONE(2, "已备案", "已备案");

    private final Integer code;
    private final String name;
    private final String desc;

    ContractFilingsStatusEnums(Integer code, String name, String info) {
        this.code = code;
        this.name = name;
        this.desc = info;
    }

    public static ContractFilingsStatusEnums getInstance(Integer code) {
        for (ContractFilingsStatusEnums value : ContractFilingsStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
