package com.yaoan.module.econtract.controller.admin.warning.vo;

import lombok.Data;

@Data
public class SignOverdueAnalysisRespVO {
    /**
     * 电子交易数量
     */
    private Long gpxQuantity;

    /**
     * 电子交易占比
     */
    private String gpxProportion;
    /**
     * 服务工程超市数量
     */
    private Long zhubajieQuantity;
    /**
     * 服务工程超市占比
     */
    private String zhubajieProportion;

    /**
     * 电子卖场数量
     */
    private Long jdmallQuantity;
    /**
     *电子卖场占比
     */
    private String jdmallProportion;

    /**
     * 框采数量
     */
    private Long gpfaQuantity;
    /**
     *框采占比
     */
    private String gpfaProportion;

    /**
     * 协议定点数量
     */
    private Long gpmallQuantity;
    /**
     * 协议定点占比
     */
    private String gpmallProportion;
    /**
     *已签订数量
     */
    private Long signedQuantity;
    /**
     *已签订占比
     */
    private String signedProportion;
    /**
     * 未签订数量
     */
    private Long unSignedQuantity;
    /**
     * 未签订占比
     */
    private String unSignedProportion;

}
