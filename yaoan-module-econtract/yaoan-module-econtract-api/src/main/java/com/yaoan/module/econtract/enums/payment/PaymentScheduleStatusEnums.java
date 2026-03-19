package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description: 付款状态 枚举类
 * @author: Pele
 * @date: 2023/12/22 13:55
 */
@Getter
public enum PaymentScheduleStatusEnums {
    /**
     * ------------- 弃用 -------------
     */
    UNPAID(0, "未开始"),
    PAYED(1, "执行中"),

    /**
     * 付款状态 枚举类
     */
    TO_PUBLISH(-1,"待发布"),
    TO_DO(0, "未开始"),
    DOING(1, "执行中"),
    //财务确认
    CONFIRM(3, "待确认"),
    DONE(4, "已完成"),

    CLOSE(5, "已关闭"),

    FROZEN(-2, "已冻结"),
    ;

    private final Integer code;
    private final String info;

    PaymentScheduleStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static PaymentScheduleStatusEnums getInstance(Integer code) {
        for (PaymentScheduleStatusEnums value : PaymentScheduleStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
