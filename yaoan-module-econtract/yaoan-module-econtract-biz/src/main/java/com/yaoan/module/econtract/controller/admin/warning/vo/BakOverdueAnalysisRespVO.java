package com.yaoan.module.econtract.controller.admin.warning.vo;

import lombok.Data;

/**
 * 备案超期分析数据返回vo
 */
@Data
public class BakOverdueAnalysisRespVO {
    /**
     * 电子交易数量
     */
    private Integer gpxQuantity;

    /**
     * 电子交易占比
     */
    private String gpxProportion;
    /**
     * 服务工程超市数量
     */
    private Integer zhubajieQuantity;
    /**
     * 服务工程超市占比
     */
    private String zhubajieProportion;

    /**
     * 电子卖场数量
     */
    private Integer jdmallQuantity;
    /**
     *电子卖场占比
     */
    private String jdmallProportion;

    /**
     * 框采数量
     */
    private Integer gpfaQuantity;
    /**
     *框采占比
     */
    private String gpfaProportion;

    /**
     * 协议定点数量
     */
    private Integer gpmallQuantity;
    /**
     * 协议定点占比
     */
    private String gpmallProportion;
    /**
     *超期已备案数量
     */
    private Integer bakOverdueNum;
    /**
     *超期已备案占比
     */
    private String bakOverdueRatio;
    /**
     * 超期未备案数量
     */
    private Integer unbakverdueNum;
    /**
     *超期未备案占比
     */
    private String unbakOverdueRatio;

}
