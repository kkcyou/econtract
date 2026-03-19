package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/12 18:15
 */
@Getter
public enum EcmsContractStatusEnums {

    /**
     * 订单状态 枚举类
     */
    DRAFT(0, "草稿"),
    TO_BE_CONFIRMED(1, "待确认"),
    CONFIRMED(2, "已确认"),
    TO_BE_SIGNED(3, "待签章"),
    SIGNED(4, "已签章"),
    UNREGISTERED(98, "未备案"),
    UNDER_REGISTRATION(99, "备案中"),
    REGISTERED(100, "已备案"),
    REMOVED(-1, "删除"),
    CANCELED(-2, "取消"),
    VOIDED(-3, "作废");

    private final Integer code;
    private final String info;

    EcmsContractStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static EcmsContractStatusEnums getInstance(Integer code) {
        for (EcmsContractStatusEnums value : EcmsContractStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
