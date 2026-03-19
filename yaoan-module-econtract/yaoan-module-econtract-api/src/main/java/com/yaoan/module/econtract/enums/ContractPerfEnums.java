package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Getter
public enum ContractPerfEnums {

    WAIT_CREATE_PERFORMANCE(1, "待建立履约"),
    WAIT_PERFORMANCE(2, "待履约"),
    IN_PERFORMANCE(3, "履约中"),
    PERFORMANCE_FINISH(4, "履约完成"),
    PERFORMANCE_PAUSE(5, "履约暂停"),
    PERFORMANCE_END(6, "履约结束"),
    PERFORMANCE_OVER_TIME(7, "履约超期"),
    OVER_TIME_PAUSE(8, "超期暂停"),
    OVER_TIME_END(9, "超期结束"),
    OVER_TIME_FINISH(10, "超期完成"),
    TERMINAT_APPROVE(11, "合同中止审批中"),
    CONTRACT_PAUSE(12, "合同中止"),
    TERMINATE_SIGNIND(13, "合同终止签署中"),
    TERMINATED(14, "合同终止"),
    FREEZE_SIGNIND(15, "合同冻结审批中"),
    FREEZED(16, "合同冻结");


    private final Integer code;
    private final String desc;

    ContractPerfEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static ContractPerfEnums getInstance(Integer code) {
        for (ContractPerfEnums value : ContractPerfEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
