package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @description:采购计划实施形式枚举
 * @author: zhc
 * @date: 2024-03-18
 */
@Getter
public enum BuyPlanImplementEnums {
    /**
     * 实施形式枚举类
     */
    PROJECT_PURCHASING("101", "一般项目采购"),
    BATCH_PURCHASING_COLLECTION("301", "批量采购归集"),
    ELECTRONIC_MALL("401", "电子卖场采购"),
    FRAMEWORK_AGREEMENT_CONTRACT_AWARD("402", "框架协议合同授予"),
    FRAMEWORK_AGREEMENT_PURCHASING("491", "协议采购"),
    SENTINEL_PURCHASING("492", "定点采购"),
    UNDER_QUOTA_PURCHASING("501", "限额以下采购"),
    URGENT_PURCHASING("502", "紧急采购"),
    CONTRACT_RENEWAL("503", "合同续签"),
    ENCORE_PRODUCT_PURCHASING("509", "安可产品采购"),
    PROJECT_MUST_TENDERED("599", "必须招标工程项目"),
    DEFAULT("0", "无限制"),
    SERVICE_ENGINEERING_SUPERMARKET("40106", "服务工程超市");

    private final String code;
    private final String info;

    BuyPlanImplementEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BuyPlanImplementEnums getInstance(String code) {
        for (BuyPlanImplementEnums value : BuyPlanImplementEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
