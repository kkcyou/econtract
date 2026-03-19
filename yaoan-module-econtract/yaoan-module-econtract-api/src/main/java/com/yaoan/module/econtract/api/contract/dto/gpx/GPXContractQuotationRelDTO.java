package com.yaoan.module.econtract.api.contract.dto.gpx;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/27 16:03
 */
@Data
public class GPXContractQuotationRelDTO {

    /**
     * 主键
     */
    private String id;

    private String quotationId;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 包ID
     */
    private String packageId;
    /**
     * 包明细id（对应PackageDetailInfo.detailId）
     */
    private String packageDetailId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划明细id
     */
    private String planDetailId;
    /**
     * 计划明细外部id（备案用）
     */
    private String outsiteId;

    /**
     * 供应商id
     */
    private String supplierGuid;
    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 服务范围
     */
    private String serviceScope;
    /**
     * 服务要求
     */
    private String serviceRequire;
    /**
     * 服务期限
     */
    private String serviceLife;
    /**
     * 服务标准
     */
    private String serviceRule;
    /**
     * 工程范围
     */
    private String workScope;
    /**
     * 施工工期
     */
    private String workPeriod;
    /**
     * 品目号
     */
    private String detailSequence;
    /**
     * 品目类型
     * A=货物,B=工程,C=服务
     */
    private String detailType;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 数量
     */
    private BigDecimal count;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 总价
     */
    private String totalPrice;
    /**
     * 品目编号
     */
    private String catalogueCode;
    /**
     * 品目名称
     */
    private String catalogueName;
    /**
     * 标的名称
     */
    private String detailName;
    /**
     * 厂商
     */
    private String producer;
}
