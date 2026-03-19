package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/25 14:51
 */
@Getter
public enum SettlementMethodEnums {
    /**
     * 结算方式 枚举类：
     */
    TRANSFER("1", "转账"),
    CASH("2", "现金"),
    CHECK("3", "支票"),
    BANK_HP("7", "银行汇票"),
    BANK_BP("8", "银行本票"),
    CREDIT_CARD("9", "信用卡结算"),
    XH("11", "信汇"),
    DH("12", "电汇"),
    WTFK("13", "委托收款"),
    TSCF("14", "托收承付"),
    CDHP("15", "承兑汇票"),
    PDTS("15", "凭单托收"),
    KJ("15", "扣缴"),
    ;

    private final String code;
    private final String info;

    SettlementMethodEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static SettlementMethodEnums getInstance(String code) {
        for (SettlementMethodEnums value : SettlementMethodEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
