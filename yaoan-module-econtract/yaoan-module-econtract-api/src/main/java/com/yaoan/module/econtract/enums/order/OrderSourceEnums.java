package com.yaoan.module.econtract.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderSourceEnums{
    /**
     * 直购(通用)
     */
    ORDER_SOURCE_ENUMS_DIRECT(0, "直接订购", 0, "直接订购",null),
    /**
     * 内蒙网上竞价(bidding)
     */
    ORDER_SOURCE_ENUMS_NMBIDDING(1, "竞价", 1, "竞价",null),
    /**
     * 议价(陕西project)
     */
    ORDER_SOURCE_ENUMS_SXBARGAIN(2, "议价", 2, "议价",null),
    /**
     * 定点竞价
     */
    ORDER_SOURCE_ENUMS_PROVIDEBIDDING(3, "定点服务", 3, "定点服务",null),
    /**
     * 询价(内蒙bidding)
     */
    ORDER_SOURCE_ENUMS_NMINQUIRY(4, "网上询价", 4, "网上询价",null),
    /**
     * 批量采购(广东)
     */
    ORDER_SOURCE_ENUMS_BATCH(5, "批量采购", 5, "批量采购",null),
    /**
     * 网上竞价(陕西project)
     */
    ORDER_SOURCE_ENUMS_SXPROJECT(6, "竞价", 1, "竞价",null),
    /**
     * 竞价(广东)
     */
    ORDER_SOURCE_ENUMS_GDPROJECT(7, "网上竞价", 1, "网上竞价",null),
    /**
     * 家具馆竞价(内蒙bidding)
     */
    ORDER_SOURCE_ENUMS_FURNITURE(8, "家具馆竞价", 1, "家具馆竞价","8"),
    /**
     * 汽车馆竞价(内蒙bidding)
     */
    ORDER_SOURCE_ENUMS_CAR(9, "汽车馆竞价", 1, "汽车馆竞价","9"),
    /**
     * 定点议价
     */
    ORDER_SOURCE_ENUMS_PROVIDEBARGAIN(12, "定点服务", 12, "定点服务",null),
    /**
     * 电子反拍(广东)
     */
    ORDER_SOURCE_ENUMS_BACKHAND(13, "电子反拍", 13, "电子反拍",null),
    /**
     * 带量采购
     */
    ORDER_SOURCE_ENUMS_VOLUMEPURCHASE(14,"带量采购", 14, "带量采购",null),
    /**
     * 工会直购
     */
    ORDER_SOURCE_UNION_DIRECT(15, "工会直购", 0, "工会直购","1000"),
    /**
     * 工会竞价
     */
    ORDER_SOURCE_UNION_BIDDING(16, "工会竞价", 1, "工会竞价","1000"),

    /**
     * 定点反拍
     */
    ORDER_SOURCE_PROVIDE_REVERSE(17, "定点反拍", 20, "定点反拍",null),

    /**
     * 多商品竞价
     */
    ORDER_SOURCE_MULTIPLE_GOODS(21, "多商品竞价", 21, "多商品竞价",null),

    /**
     * 灯具馆竞价
     */
    ORDER_SOURCE_LAMPS_BIDDING(22, "灯具馆竞价",1,"灯具馆竞价","1100"),


    //山西 推送合同公示时区分 项目类型使用 t_provideproject 项目类型：23直购
    PROVIDEPROJECT_SHANXI_ZG(23, "项目类型：23直购",23,"项目类型：23直购",null)

    ;

    final Integer code;
    final String value;
    final Integer transactionMethod;
    final String name;
    final String platform;


    public static OrderSourceEnums getInstance(Integer code) {
        for (OrderSourceEnums value : OrderSourceEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}