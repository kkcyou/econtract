package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @author doujiale
 */

@Getter
public enum ContractStatusEnums {
    /**
     * 合同状态 枚举类
     */
    TO_BE_SENT(0, "待发送"),
    BE_SENT_BACK(1, "被退回"),
    SENT(2, "已发送"),

    //（待发起方发起签署，发起方确认之前）
    TO_BE_CONFIRMED(3, "待确认"),
    //（发起方确认之后）
    TO_BE_SIGNED(4, "待签署"),

    SIGN_REJECTED(5, "已拒签"),
    SIGN_COMPLETED(6, "签署完成"),//也是待履约
    SIGN_OVERDUE(7, "逾期未签署"),
    TERMINATE_SIGNIND(8, "合同终止签署中"),
    TERMINATED(9, "合同终止"),
    CONTRACT_CHANGE(10, "合同变更"),

    //-------------创建合同-----新增审核阶段------------------------合同补录后审批共用-------------------
    TO_BE_CHECK(11, "待送审"),
    CHECKING(12, "审核中"),
    CHECK_REJECTED(13, "审核未通过"),
    APPROVE_BACK(14, "审批被退回"),

    FREEZE_SIGNIND(15, "冻结签署中"),
    FREEZED(16, "合同冻结中"),



    /**
     * 履约管理
     * */
    PERFORMANCE_CLOSURE(2000, "已关闭"),
    PERFORMING(2001, "履约中"),
    PERFORMANCE_COMPLETE(2002, "履约完成"),
    PERFORMANCE_RISK(2003, "履约风险"),
    PERFORMANCE_RISK_DISPUTE(2004, "履约争议"),
    PERFORMANCE_RISK_PAUSE(2005, "履约暂停"),
    PERFORMANCE_RISK_EXTENSION(2006, "履约延期"),
    PERFORMANCE_RISK_OVERDUE(2007, "履约逾期"),
    /**
     * 合同变动
     * */
    CONTRACT_CLOSING(2008,"冻结中"),

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
    CONTRACT_AUDITSTATUS_CANCELLATION(-3, "已作废"),

    /**
     * 未签署
     */
    CONTRACT_AUDITSTATUS_NOT_SIGNED(17, "未签署"),
    /**
     * 用印审批中
     */
    CONTRACT_AUDITSTATUS_SEAL_APPROVAL(18, "用印审批中"),


    ;


    private final Integer code;
    private final String desc;

    ContractStatusEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static ContractStatusEnums getInstance(Integer code) {
        for (ContractStatusEnums value : ContractStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
