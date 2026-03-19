package com.yaoan.module.econtract.enums.warning;

import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import lombok.Getter;

@Getter
public enum WarningBusinessEnum {

    CONTRACT_MANAGE("HTGL-0001", "contractQueryStrategy" ,"合同管理"),
    CONTRACT_DRAFT("HTGL-0100", "contractQueryStrategy" ,"合同管理"),
    CONTRACT_APPROVE("HTGL-0101", "contractApproveQueryStrategy" ,"合同审批"),
    CONTRACT_SIGN("HTGL-0102", "contractQueryStrategy" ,"合同签订"),
    CONTRACT_ARCHIVE("HTGL-0103", "contractQueryStrategy" ,"合同归档"),
    CONTRACT_BORROW("HTGL-0104", "contractBorrowQueryStrategy" ,"合同归档"),
    CONTRACT_CONTINUE("HTGL-0105", "contractQueryStrategy" ,"合同续签"),
    CONTRACT_PERFORMANCE("HTLY-0001", "acceptanceQueryStrategy" ,"合同履约"),
    CONTRACT_PERF_CHECK("HTLY-0101", "paymentScheduleQueryStrategy" ,"履约验收"),
    CONTRACT_PAYMENT("HTLY-0102", "paymentScheduleQueryStrategy" ,"合同支付"),
    CONTRACT_RECIEVE("HTLY-0103", "paymentScheduleQueryStrategy" ,"合同收款"),
    ;


    private final String code;          //模块code编码
    private final String factoryName;   // 对应策略工厂名称
    private final String desc;          // 描述

    WarningBusinessEnum(String code, String factoryName, String desc) {
        this.code = code;
        this.factoryName = factoryName;
        this.desc = desc;
    }
    public static WarningBusinessEnum getInstance(String code) {
        for (WarningBusinessEnum value : WarningBusinessEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
