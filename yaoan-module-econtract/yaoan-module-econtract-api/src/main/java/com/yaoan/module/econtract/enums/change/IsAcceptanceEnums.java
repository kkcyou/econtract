package com.yaoan.module.econtract.enums.change;

import com.yaoan.module.econtract.enums.common.IfEnums;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-2 18:35
 */
@Getter
public enum IsAcceptanceEnums {
    /**
     * 是否 的枚举
     */
    TO_DO(0, "待发起验收"),

    DONE(1, "已发起验收"),

    CLOSED(5, "已关闭验收"),



    ;

    private final Integer code;
    private final String info;

    IsAcceptanceEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static IsAcceptanceEnums getInstance(Integer code) {
        for (IsAcceptanceEnums value : IsAcceptanceEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
