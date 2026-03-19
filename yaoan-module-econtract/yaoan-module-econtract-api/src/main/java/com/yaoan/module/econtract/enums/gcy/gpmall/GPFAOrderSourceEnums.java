package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 数采-框采订单来源枚举
 * @version 1.0
 * @date 2021-4-10 11:41
 */
@AllArgsConstructor
@Getter
public enum GPFAOrderSourceEnums {

    ORDER_SOURCE_ENUMS_COMMON(-1, "【非真实订单来源】合同默认(通用)"),
    ORDER_SOURCE_ENUMS_DIRECT(0, "直接订购(通用)"),

    ORDER_SOURCE_ENUMS_NMBIDDING(1, "内蒙网上竞价(bidding)"),
    ORDER_SOURCE_ENUMS_SXBARGAIN(2, "议价(陕西project)"),
    ORDER_SOURCE_ENUMS_PROVIDEBIDDING(3, "定点服务(竞价)"),
    ORDER_SOURCE_ENUMS_NMINQUIRY(4, "网上询价(内蒙bidding)"),
    ORDER_SOURCE_ENUMS_BATCH(5, "批量采购(广东)"),
    ORDER_SOURCE_ENUMS_SXPROJECT(6, "网上竞价(陕西project)"),
    ORDER_SOURCE_ENUMS_GDPROJECT(7, "网上竞价(广东)"),
    ORDER_SOURCE_ENUMS_FURNITURE(8, "家具用具(内蒙bidding)"),
    ORDER_SOURCE_ENUMS_CAR(9, "汽车馆用具(内蒙bidding)"),
    ORDER_SOURCE_ENUMS_PROVIDEBARGAIN(12, "定点服务(议价)"),
    ORDER_SOURCE_ENUMS_BACKHAND(13, "电子反拍(广东)"),
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
     * 货物类二次竞价
     */
    ORDER_SOURCE_GPFA_PROJECT(17, "货物类二次竞价"),

    /**
     * 顺序轮候
     */
    ORDER_SOURCE_QUEUING(18, "顺序轮候"),

    /**
     * 服务类直接选定
     */
    ORDER_SOURCE_PROVIDE_DIRECT(19, "服务类直接选定"),

    /**
     * 服务类二次竞价
     */
    ORDER_SOURCE_PROVIDE_SECOND_BIDDING(20, "服务类二次竞价"),
    ;

    final Integer code;
    final String value;

    /**
     * 根据value找到对应的valuename
     *
     * @param code
     * @return
     */
    public static String valuesOf(Integer code) {
        for (GPFAOrderSourceEnums model : values()) {
            if (model.code.equals(code)) {
                return model.value;
            }
        }
        return "";
    }
}
