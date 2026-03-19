package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 电子卖场提供的合同状态的枚举类,TransactionContractStatusEnums为电子交易的合同状态枚举
 *
 * @author Tdb
 * @version 1.0
 * @date 2021-4-10 11:41
 */
@Getter
public enum ContractAuditStatusEnums {

    /**
     * 被退回
     */
    CONTRACT_AUDITSTATUS_RETURN(-2, "被退回"),

    CONTRACT_CONFIRM_BACK(2001,"确认被退回"),

    CONTRACT_SIGN_BACK(3001,"签章被退回"),

    /**
     * 已取消
     */
    CONTRACT_AUDITSTATUS_CANCEL(-1, "已取消"),

    /**
     * 待发送
     */
    TO_BE_CHECK(11000000, "待发送"),

    /**
     * 已发送
     */
    CONTRACT_AUDITSTATUS_TOBECONFIRMED(1, "已发送"),

    /**
     * 已确认 相对方确认
     */
    CONTRACT_AUDITSTATUS_CONFIRMED(2, "已确认"),

    /**
     * 发起方确认-前端展示-待对方确认
     */
    CONTRACT_AUDITSTATUS_CONFIRM(12, "发起方确认"),

    /**
     * 供应商盖章
     */
    CONTRACT_AUDITSTATUS_DELIVERED(3, "供应商盖章"),

    /**
     * 完成
     */
    CONTRACT_AUDITSTATUS_RECEIVED(4, "完成"),


    /**
     * 已退货
     */
    CONTRACT_AUDITSTATUS_REFUND(99, "已退货"),

    /**
     * 已备案
     */
    CONTRACT_AUDITSTATUS_RECORD(100, "已备案"),

    /**
     * 已作废
     */
    CONTRACT_AUDITSTATUS_CANCELLATION(101, "已作废"),

    /**
     * 待送审
     */
    CONTRACT_AUDITSTATUS_TOBESENT(0, "草稿"),
    CHECKING(12000000, "审核中"),
    CHECK_REJECTED(13000000, "审核未通过");


    private final Integer code;
    private final String info;

    ContractAuditStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractAuditStatusEnums getInstance(Integer code) {
        for (ContractAuditStatusEnums value : ContractAuditStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
