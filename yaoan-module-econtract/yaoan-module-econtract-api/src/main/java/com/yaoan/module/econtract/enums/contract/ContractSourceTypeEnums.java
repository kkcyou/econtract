package com.yaoan.module.econtract.enums.contract;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/18 16:23
 */
@Getter
public enum ContractSourceTypeEnums {
    /**
     * 合同来源类型 的枚举
     */
    ELECTRIC(0, "电子合同"),

    UPLOAD(1, "上传合同"),


    ;

    private final Integer code;
    private final String info;

    ContractSourceTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractSourceTypeEnums getInstance(Integer code) {
        for (ContractSourceTypeEnums value : ContractSourceTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
