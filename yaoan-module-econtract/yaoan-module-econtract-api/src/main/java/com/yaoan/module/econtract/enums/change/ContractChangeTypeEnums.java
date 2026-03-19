package com.yaoan.module.econtract.enums.change;

import com.yaoan.module.econtract.enums.BusinessEnums;
import lombok.Getter;

/**
 * @description: 合同变动类型 枚举
 * @author: Pele
 * @date: 2024/1/24 16:24
 */
@Getter
public enum ContractChangeTypeEnums {
    /**
     * 合同变动类型 枚举
     */
    CHANGE(1, "变更"),
    SUPPLEMENT(2, "补充"),
    TERMINATE(3, "解除"),
    TERMINATED(4, "关闭"),
    CANCEL(5, "取消"),
    CANCELLATION(6, "作废"),
    ;

    private final Integer code;
    private final String info;

    ContractChangeTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractChangeTypeEnums getInstance(Integer code) {
        for (ContractChangeTypeEnums value : ContractChangeTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
