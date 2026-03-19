package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 合同管理列表-合同状态标识
 *
 * @author zhc
 * @version 1.0
 * @date 2024-04-24
 */
@Getter
public enum ContractStatusFlagEnums {
////状态标识 0：草稿 1：已发送 2：已确认待采购人盖章 3：已确认待供应商盖章
// 4：供应商已盖章待采购人盖章 5：采购人已盖章待供应商盖章 6：已取消 7：已备案 8：未备案
//
    /**
     * 被退回
     */
    NO_SEND(0, "草稿"),
    SEND(1, "已发送"),
    SURE_WAIT_BUYER_SIGN(2, "已确认待采购人盖章"),
    SURE_WAIT_SUP_SIGN(3, "已确认待供应商盖章"),
    SIGN_WAIT_BUYER_SIGN(4, "供应商已盖章待采购人盖章"),
    SIGN_WAIT_SUP_SIGN(5, "采购人已盖章待供应商盖章"),
    CANCEL(6, "已取消"),
    BAKED(7, "已备案"),
    BAKING(8, "备案中"),
    NO_BAK(9, "未备案"),
    TO_BE_CONFIRMED(10, "待确认");


    private final Integer code;
    private final String info;

    ContractStatusFlagEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractStatusFlagEnums getInstance(Integer code) {
        for (ContractStatusFlagEnums value : ContractStatusFlagEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
