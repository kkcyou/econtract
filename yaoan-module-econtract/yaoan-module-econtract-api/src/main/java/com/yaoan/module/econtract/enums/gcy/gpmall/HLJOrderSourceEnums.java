package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @author 电子合同系统统一 交易方式
 * @version 1.0
 * @date 2021-4-10 11:41
 */
@Getter
public enum HLJOrderSourceEnums {
    /**
     * 直接订购(通用)
     */
    ORDER_SOURCE_ENUMS_DIRECT(0, "直接订购"),
    /**
     * 网上竞价
     */
    ORDER_SOURCE_ENUMS_NMBIDDING(1, "竞价"),
    ORDER_SOURCE_ENUMS_SXBARGAIN(2, "议价(陕西project)"),
    /**
     * 定点竞价
     */
    ORDER_SOURCE_ENUMS_PROVIDEBIDDING(3, "定点服务（竞价）"),
    ORDER_SOURCE_ENUMS_NMINQUIRY(4, "网上询价"),
    ORDER_SOURCE_ENUMS_BATCH(5, "批量采购(广东)"),
    /**
     * 网上竞价(陕西project)
     */
//    ORDER_SOURCE_ENUMS_SXPROJECT(6, "竞价(陕西project)"),
    /**
     * 竞价(广东)
     */
//    ORDER_SOURCE_ENUMS_GDPROJECT(7, "网上竞价(广东)"),
    /**
     * 家具馆竞价(内蒙bidding)
     */
    ORDER_SOURCE_ENUMS_FURNITURE(8, "家具馆竞价"),
    /**
     * 汽车馆竞价(内蒙bidding)
     */
    ORDER_SOURCE_ENUMS_CAR(9, "汽车馆竞价"),
    /**
     * 定点服务(议价)
     */
//    ONLINE_BARGAINING(10, "网上议价"),
    ORDER_SOURCE_ENUMS_PROVIDEBARGAIN(12, "定点服务(议价)"),
    ORDER_SOURCE_ENUMS_BACKHAND(13, "电子反拍"),
    ORDER_SOURCE_ENUMS_VOLUMEPURCHASE(14,"带量采购"),
    /**
     * 工会直购
     */
    ORDER_SOURCE_UNION_DIRECT(15, "工会直购"),
    /**
     * 工会竞价
     */
    ORDER_SOURCE_UNION_BIDDING(16, "工会竞价"),

    /**
     * 定点反拍
     */
    ORDER_SOURCE_PROVIDE_REVERSE(17, "定点反拍"),

    /**
     * 场内直购（电子卖场）
     */
    DIRECT_FLOOR_PURCHASE(18, "场内直购（电子卖场）"),
    /**
     * 场内比价（电子卖场）
     */
    FLOOR_RATE(19, "场内比价（电子卖场）"),

    /**
     * 多商品竞价
     */
    ORDER_SOURCE_MULTIPLE_GOODS(21, "多商品竞价"),
    /**
     * 灯具馆竞价
     */
    ORDER_SOURCE_LAMPS_BIDDING(22, "灯具馆竞价"),


    //山西 推送合同公示时区分 项目类型使用 t_provideproject 项目类型：23直购
    PROVIDEPROJECT_SHANXI_ZG(23, "项目类型：23直购"),
    /**
     * 服务类二次竞价
     */
    ORDER_SOURCE_PROVIDE_SECOND_BIDDING(24, "服务类二次竞价"),
    /**
     * 服务类直接选定
     */
    ORDER_SOURCE_PROVIDE_DIRECT(25, "服务类直接选定"),
    /**
     * 顺序轮候
     */
    ORDER_SOURCE_QUEUING(26, "顺序轮候"),
    /**
     * 货物类二次竞价
     */
    ORDER_SOURCE_GPFA_PROJECT(27, "货物类二次竞价"),
    ORDER_SOURCE_ENUMS_CAR2(29, "汽车馆用具(内蒙bidding)"),
    ORDER_SOURCE_ENUMS_FURNITURE2(30, "家具用具(内蒙bidding)"),
    INSIDE_BACKSHOT(31, " 场内反拍（电子卖场）"),
    INSIDE_GROUP_PURCHASE(32, " 场内团购（电子卖场）"),
    ON_AND_OFF_PRICE(33, " 场内外比价（电子卖场）");

    private final Integer code;
    private final String info;

    HLJOrderSourceEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static HLJOrderSourceEnums getInstance(Integer code) {
        for (HLJOrderSourceEnums value : HLJOrderSourceEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
