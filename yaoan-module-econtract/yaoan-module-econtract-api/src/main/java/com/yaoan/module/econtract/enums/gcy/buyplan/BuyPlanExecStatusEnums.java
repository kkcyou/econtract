package com.yaoan.module.econtract.enums.gcy.buyplan;

import lombok.Getter;

/**
 * @description:
 * @author: doujl
 * @date: 2023/12/12 18:15
 */
@Getter
public enum BuyPlanExecStatusEnums {

    /**
     * 订单状态 枚举类
     */
    TO_RECEIVE("0", "计划待接收"),
    RECEIVED("1", "计划已接收（执行中）"),
    REJECTED("2", "计划已退回"),
    NOT_EXCHANGE("3", "不交换"),
    ;

    private final String code;
    private final String info;

    BuyPlanExecStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BuyPlanExecStatusEnums getInstance(String code) {
        for (BuyPlanExecStatusEnums value : BuyPlanExecStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
