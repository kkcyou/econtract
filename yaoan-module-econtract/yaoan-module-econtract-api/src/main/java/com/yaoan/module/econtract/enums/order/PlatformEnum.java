package com.yaoan.module.econtract.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 1	集采馆
 * 11	防疫馆
 * 12	警用装备馆
 * 14	带量采购专区
 * 1500	民优特产专区
 * 2	扶贫馆
 * 3	定点服务
 * 4	超市
 * 5	消防馆
 * 6	教育馆
 * 7	公安
 * 8	家具馆
 * 9	汽车馆
 *
 * @author lwh
 * @program gpmall-bpoc
 * @description 平台platform枚举
 * @create 2021-07-12 17:24
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum  {

    /**
     * 平台
     */
    PLATFORM(0, "平台"),

    /**
     * 集采馆
     */
    JCG_PLATFORM(1, "集采馆"),

    /**
     * 扶贫馆(乡村振新馆)
     */
    FPG_PLATFORM(2, "扶贫馆"),

    /**
     * 定点服务
     */
    DDFW_PLATFORM(3, "定点服务"),

    /**
     * 网上超市
     */
    WSCS_PLATFORM(4, "网上超市"),

    /**
     * 消防馆
     */
    XFG_PLATFORM(5, "消防馆"),

    /**
     * 教育馆
     */
    JYG_PLATFORM(6, "教育馆"),

    /**
     * 公安
     */
    POLICE_PLATFORM(7, "公安"),

    /**
     * 家居馆
     */
    FURNITURE_PLATFORM(8, "家居馆"),

    /**
     * 汽车馆
     */
    VEHICLE_PLATFORM(9, "汽车馆"),

    /**
     * 防疫馆
     */
    FYG_PLATFORM(11, "防疫馆"),

    /**
     * 警备馆平台
     */
    JBG_PLATFORM(12, "警备馆平台"),

    /**
     * 带量专区平台
     */
    DLCG_PLATFORM(14, "带量专区平台"),

    /**
     * 工会专区
     */
    UNION_PLATFORM(1000, "工会专区"),

    /**
     * 工会乡村振兴
     */
    UNION_COUNTRY_REVITALIZE_PLATFORM(1002, "工会乡村振兴"),

    /**
     * 工会家具
     */
    UNION_FURNITURE_PLATFORM(1008, "工会家具"),

    /**
     * 工会汽车
     */
    UNION_CART_PLATFORM(1009, "工会汽车"),

    /**
     * 工会灯具
     */
    UNION_LAMPS_PLATFORM(1011, "工会灯具"),

    /**
     * 灯具馆
     */
    LAMPS_PLATFORM(1100, "灯具馆"),

    /**
     * 民优特产专区
     */
    SX_MYTCZQ_PLATFORM(1500, "民优特产专区"),

    /**
     * 深汕联合馆
     */
    SS_UNITED_PLATFORM(2100,"深汕联合馆"),

    /**
     * 货柜机
     */
    CONTAINER_PLATFORM(1900, "货柜机"),

    /**
     * 制造业发展馆
     */
    HIGH_QUALITY(4000, "制造业发展馆"),

    /**
     * 中山馆
     */
    HIGH_QUALITY_ZS(4001, "中山馆"),

    /**
     * 东莞馆
     */
    HIGH_QUALITY_DW(4002, "东莞馆"),

    /**
     * 珠海馆
     */
    HIGH_QUALITY_ZH(4003, "珠海馆"),

    /**
     * 惠州馆
     */
    HIGH_QUALITY_HZ(4004, "惠州馆"),

    /**
     * 江门馆
     */
    HIGH_QUALITY_JM(4005, "江门馆"),

    /**
     * 批量采购场馆
     */
    BATCH_PURCHASING(3000, "批量采购场馆"),

    /**
     * 绿色建材馆
     */
    GREEN_BUILDING_PURCHASING(3110, "绿色建材馆"),

    XZLZG(2001, "西藏林芝馆"),

    ;

    private final Integer code;
    private final String value;

    /**
     * 遍历查询对应的场馆名称
     *
     * @param code code
     * @return value
     * @author jy
     * @date 2021-12-13
     */
    public static String getValue(Integer code) {
        return Stream.of(values()).filter(item -> item.code.equals(code)).findFirst().orElse(PlatformEnum.PLATFORM).value;
    }
    public static PlatformEnum getInstance(Integer code) {
        for (PlatformEnum value : PlatformEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
