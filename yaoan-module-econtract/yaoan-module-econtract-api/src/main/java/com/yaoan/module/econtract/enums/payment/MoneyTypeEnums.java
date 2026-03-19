package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description: 首付款1 进度款2 尾款3
 * @author: Pele
 * @date: 2024/9/23 20:33
 */
@Getter
public enum MoneyTypeEnums {
    /**
     * 款项类型 枚举类
     */
    DOWN_PAYMENT(1, "首付款"),
    PROGRESS_PAYMENT(2, "进度款"),
    FINAL_PAYMENT(3, "尾款"),
    RETENTION_PAYMENT(4, "质保金"),
    ;

    private final Integer code;
    private final String info;

    MoneyTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static MoneyTypeEnums getInstance(Integer code) {
        for (MoneyTypeEnums value : MoneyTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
