package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/6 20:05
 */
@Getter
public enum StatusEnums {

    /**
     * 四种审批状态
     */
//模板
// ”未审批“ ：删除和更改
// ”未通过审批“：只能编辑。
//    NEVER_APPROVED(0, "待送审"),
//    APPROVING(1, "审批中"),
//    APPROVE_DENY(2, "审批未通过"),
//    APPROVED(3, "审批通过");

    NEVER_APPROVED(0, "待送审"),
    APPROVING(1, "审批中"),
    APPROVED(2, "审批通过"),
    APPROVE_DENY(5, "审批未通过"),


    ;

    private final Integer code;
    private final String info;

    StatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static StatusEnums getInstance(Integer code) {
        for (StatusEnums value : StatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
