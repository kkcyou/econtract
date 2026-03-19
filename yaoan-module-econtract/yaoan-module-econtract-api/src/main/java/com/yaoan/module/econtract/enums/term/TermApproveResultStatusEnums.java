package com.yaoan.module.econtract.enums.term;

import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import lombok.Getter;

/**
 * @description: 条款审批状态枚举
 * @author: Pele
 * @date: 2024/1/10 10:29
 */
@Getter
public enum TermApproveResultStatusEnums {
    /**
     * 付款类型 枚举类
     */
    TO_SEND(0,  "草稿"),
    APPROVING(1,  "审批中"),
    SUCCESS(2, "审批通过"),
    //进入审批流的任务，只可能结束节点变为退回
    REJECTED(5,  "被退回")
    ;

    ;

    private final Integer code;
    private final String info;

    TermApproveResultStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static TermApproveResultStatusEnums getInstance(Integer code) {
        for (TermApproveResultStatusEnums value : TermApproveResultStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
