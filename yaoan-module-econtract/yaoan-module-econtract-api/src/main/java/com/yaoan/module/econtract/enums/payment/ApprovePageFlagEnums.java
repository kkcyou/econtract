package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/27 16:01
 */
@Getter
public enum ApprovePageFlagEnums {
    /**
     * 付款审批列表标识 枚举类
     */
    ALL(0, "全部"),
    DONE(1, "已审批"),
    TO_DO(2, "未审批"),

    ;

    private final Integer code;
    private final String info;

    ApprovePageFlagEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ApprovePageFlagEnums getInstance(Integer code) {
        for (ApprovePageFlagEnums value : ApprovePageFlagEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
