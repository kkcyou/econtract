package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 电子合同统一合同状态枚举
 *
 * @author Tdb
 * @version 1.0
 * @date 2021-4-10 11:41
 */
@Getter
public enum HLJContractStatusEnums {
    /**
     * 黑龙江-合同状态枚举
     */
    CONTRACT_AUDITSTATUS_TOBESENT(0, "草稿"),
    /**
     * 已发送/待确认
     */
    CONTRACT_AUDITSTATUS_TOBECONFIRMED(1, "已发送"),
    /**
     * 已确认
     */
    CONTRACT_AUDITSTATUS_SURE(2, "已确认待采购人盖章"),
    CONTRACT_AUDITSTATUS_SURE2(23, "已确认待供应商盖章"),
    /**
     * 待盖章
     */
//    CONTRACT_AUDITSTATUS_DELIVERED(3, "待盖章"),
    CONTRACT_AUDITSTATUS_DELIVERED(3, "供应商已盖章待采购人盖章"),
    BUYER_SIGNED(34, "采购人已签章"),
    /**
     * 完成
     */
//    CONTRACT_AUDITSTATUS_RECEIVED(4, "已盖章"),
    CONTRACT_AUDITSTATUS_RECEIVED(4, "采购人已盖章待供应商盖章"),

    /**
     * 待代理机构审核
     */
    TO_BE_CONFIRMED_BY_AGENCY(66, "待代理机构审核"),

    /**
     * 未备案
     */
    CONTRACT_AUDITSTATUS_NO_RECORD(98, "未备案"),
    /**
     * 备案中
     */
    CONTRACT_AUDITSTATUS_RECORDING(99, "备案中"),
    /**
     * 已备案
     */
    CONTRACT_AUDITSTATUS_RECORDED(100, "已备案"),
    /**
     * 已删除
     */
    CONTRACT_AUDITSTATUS_DELETE(-1, "已删除"),
    /**
     * 已取消
     */
    CONTRACT_AUDITSTATUS_CANCEL(-2, "已取消"),
    /**
     * 已作废
     */
    CONTRACT_AUDITSTATUS_CANCELLATION(-3, "已作废");


    private final Integer code;
    private final String info;

    HLJContractStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static HLJContractStatusEnums getInstance(Integer code) {
        for (HLJContractStatusEnums value : HLJContractStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
