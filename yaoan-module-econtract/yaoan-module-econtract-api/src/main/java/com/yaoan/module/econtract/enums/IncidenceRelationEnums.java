package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Getter
public enum IncidenceRelationEnums {

    OUTLINE_AGREEMENT(1, "框架协议"),
    CONTRACT_CHANGE_AGREEMENT(2, "合同变更协议"),
    CONTRACT_RENEWAL_AGREEMENT(3, "合同续签协议"),
    CONTRACT_REPLENISH_AGREEMENT(4, "补充协议"),
    CONTRACT_STOP_AGREEMENT(5, "合同终止"),
    OTHER_AGREEMENT(6, "其他协议"),
   SON_CONTRACT(7, "子合同"),
    PARENT_CONTRACT(8, "主合同");



    private final Integer code;
    private final String desc;

    IncidenceRelationEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static IncidenceRelationEnums getInstance(Integer code) {
        for (IncidenceRelationEnums value : IncidenceRelationEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
