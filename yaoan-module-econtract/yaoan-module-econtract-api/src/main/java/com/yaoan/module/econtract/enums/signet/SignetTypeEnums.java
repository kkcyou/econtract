package com.yaoan.module.econtract.enums.signet;

import lombok.Getter;


@Getter
public enum SignetTypeEnums {
    /**
     * 印章类型 枚举类
     */
    OFFICIAL_SEAL(1, "公章"),
    CONTRACT_SEAL(2, "合同专用章"),
    FINANCE_SEAL (3, "财务专用章"),
    HR_SEAL(4, "人事专用章"),
    COMPANY_SEAL(5, "法人章"),
    OTHER(6, "其他 ");

    private final Integer code;
    private final String desc;

    SignetTypeEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static SignetTypeEnums getInstance(Integer code) {
        for (SignetTypeEnums value : SignetTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getDesc(Integer code) {
        for (SignetTypeEnums value : SignetTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

}
