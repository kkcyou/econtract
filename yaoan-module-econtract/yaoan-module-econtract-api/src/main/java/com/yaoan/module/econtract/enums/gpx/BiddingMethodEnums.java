package com.yaoan.module.econtract.enums.gpx;

import lombok.Getter;

/**
 * @description: 招标项目方式名称
 * @author: Pele
 * @date: 2024/5/30 16:40
 */
@Getter
public enum BiddingMethodEnums {
    /**
     * 招标项目方式名称：
     * common:一般项目采购
     * batch:批量集中采购
     * union:联合采购
     * other:其他
     */

    COMMON("common", "一般项目采购"),
    BATCH("batch", "批量集中采购"),
    UNION("union", "联合采购"),
    OTHER("other", "其他"),
    ;

    private final String code;
    private final String info;

    BiddingMethodEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BiddingMethodEnums getInstance(String code) {
        for (BiddingMethodEnums value : BiddingMethodEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
