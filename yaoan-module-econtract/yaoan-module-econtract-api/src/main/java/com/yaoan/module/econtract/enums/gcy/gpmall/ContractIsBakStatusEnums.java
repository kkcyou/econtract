package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 合同备案状态枚举类
 *
 * @author zhc
 * @date 2023-03-21
 */
@Getter
public enum ContractIsBakStatusEnums {

    YES(1, "已备案"),

    NO(0, "未备案"),

    BAKING(3, "备案中"),

    FAILURE(-1, "备案失败"),

    NO_NEED(-2, "无需备案");


    private final Integer code;
    private final String info;

    ContractIsBakStatusEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractIsBakStatusEnums getInstance(Integer code) {
        for (ContractIsBakStatusEnums value : ContractIsBakStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
