package com.yaoan.module.econtract.enums.payment;

import com.yaoan.module.econtract.enums.AccountStatus;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/22 9:57
 */
@Getter
public enum PaymentApplicationTimeEnums {
    /**
     *
     */
    yesterday ("0", "昨天"),
    today ("1", "今天"),
    tomorrow ("2", "明天"),
    this_week ("3", "本周"),
    last_week ("4", "上周"),
    next_week ("5", "下周"),
    this_month ("6", "未激活"),
    all ("7", "未激活"),

    ;

    private final String code;
    private final String info;

    PaymentApplicationTimeEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static PaymentApplicationTimeEnums getInstance(String code) {
        for (PaymentApplicationTimeEnums value : PaymentApplicationTimeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
