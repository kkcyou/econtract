package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum GPMallAlertEnums {
    /**
     * 合同待办提醒 枚举类
     */
    ALERT_STAGE_CONFIRM_REJECT(1, "被退回", "确认阶段"),
    ALERT_STAGE_TO_BE_CONFIRM(3, "待确认", "确认阶段"),
    ALERT_STAGE_CONFIRM_APPROVE(4, "已确认", "签章阶段"),
    ALERT_STAGE_SIGN_APPROVE(6, "签署完成", "签章阶段"),
    ALERT_STAGE_TO_BE_CHECK(11, "待送审", "内部审核阶段"),
    ALERT_STAGE_CHECKING(12, "审核中", "内部审核阶段"),
    ALERT_STAGE_CHECK_REJECTED(13, "审核未通过", "内部审核阶段"),
    ALERT_STAGE_APPROVE_BACK(14, "审批被退回", "内部审核阶段"),
    ;
    private final Integer code;
    private final String info;
    private final String stageName;

    GPMallAlertEnums(Integer code, String info, String stageName) {
        this.code = code;
        this.info = info;
        this.stageName = stageName;
    }

    public static GPMallAlertEnums getInstance(Integer code) {
        for (GPMallAlertEnums value : GPMallAlertEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
