package com.yaoan.module.econtract.controller.admin.warning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 辅助计算DTO
 * @author doujiale
 */
@Data
public class ContractBakNumInfoRespVO implements Serializable {

    private static final long serialVersionUID = 4449398546218064870L;
    /**
     * 备案总数（合同签订完成需要备案的数量）
     */
    @Schema(description = "备案总数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer bakTotalNum;

    /**
     * 已备案数量
     */
    @Schema(description = "已备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer okBakTotalNum;
    /**
     * 超期已备案数量
     */
    @Schema(description = "超期已备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overOkBakNum;
    /**
     * 未超期已备案数量
     */
    @Schema(description = "未超期已备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer noOverOkBakNum;

    /**
     * 未备案数量
     */
    @Schema(description = "未备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer noBakTotalNum;
    /**
     * 超期未备案数量
     */
    @Schema(description = "超期未备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overnoBakNum;
    /**
     * 未超期未备案数量
     */
    @Schema(description = "未超期未备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer noOvernoBakNum;
    /**
     * 备案超期占比
     */
    @Schema(description = "备案超期占比", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String overBakNumRatio;
    /**
     * 采购人名称
     */
    @Schema(description = "采购人(采购单位)名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;
    /**
     * 采购人id
     */
    @Schema(description = "采购人id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgId;
    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionName;
    /**
     * 区划编号
     */
    @Schema(description = "区划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

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
