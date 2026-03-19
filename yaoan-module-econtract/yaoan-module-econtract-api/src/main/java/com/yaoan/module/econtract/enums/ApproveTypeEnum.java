package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @author doujiale
 */

@Getter
public enum ApproveTypeEnum {
    /**
     * 履约相关枚举类
     */
    SUSPEND("1", "履约中止"),


    /**
     * 模板相关枚举类
     */
    APPROVING("201", "待审批");

    private final String code;
    private final String info;

    ApproveTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ApproveTypeEnum getInstance(String code) {
        for (ApproveTypeEnum value : ApproveTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
