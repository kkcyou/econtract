package com.yaoan.module.econtract.controller.admin.warning.vo;

import lombok.Data;

@Data
public class ContractCountingRespVO {
    /**
     * 合同数量
     */
    private Long contractQuantity;

    /**
     * 已签订数量
     */
    private Long signedQuantity;

    /**
     * 未签订数量
     */
    private Long unSignedQuantity;

    /**
     * 已签订超期数量
     */
    private Long signedOverdueQuantity;

    /**
     * 已签订未超期数量
     */
    private Long signedNoOverdueQuantity;

    /**
     * 未签订超期数量
     */
    private Long unSignedOverdueQuantity;

    /**
     * 未签订未超期数量
     */
    private Long unSignedNoOverdueQuantity;

    /**
     * 超期签订占比
     */
    private String signedOverdueProportion;

    /**
     * 采购单位id
     */
    private String buyerOrgId;

    /**
     * 采购人名称
     */
    private String buyerOrgName;
    /**
     * 采购人区划
     */
    private String regionCode;
    /**
     * 采购人区划名称
     */
    private String regionName;

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
     *已签订占比
     */
    private String signedProportion;

    /**
     * 未签订占比
     */
    private String unSignedProportion;






}
