package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 15:17
 */
@Data
public class BidConfirmQuotationDetail {

    private String id;
    /**
     * 包明细id（对应PackageDetailInfo.detailId）
     */
    private String packageDetailId;
//    /**
//     * 供应商id
//     */
//    private String supplierGuid;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 标的名称
     */
    private String detailName;
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
     * 项目经理要求
     */
    private String projectManagerRequire;
    /**
     * 职业证书要求
     */
    private String certificateRequire;
    /**
     * 品目号
     */
    private String detailSequence;
    /**
     * 品目编号
     */
    private String packageSequence;
    /**
     * 品目类型 (A=货物,B=工程,C=服务)
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
     * 产品产地
     */
    private String productOrigin;
    /**
     * 型号规格
     */
    private String modelSpecification;
    /**
     * 品目名称
     */
    private String categoryName;
    /**
     * 厂商
     */
    private String producer;
    /**
     * 报价次数
     */
    private Short quotationCount;
    /**
     * 售后服务
     */
    private String saleService;
    /**
     * 区划
     */
    private String zoneCode;
    /**
     * 区划名称
     */
    private String zoneName;
    /**
     * 编辑状态
     */
    private Integer editStatus;
    /**
     * 总价
     */
    private String totalPrice;
    /**
     * 价格存储方式
     */
    private BigDecimal priceMethod;
    /**
     * 技术参数
     */
    private String detailParameter;
    /**
     * 是否进口产品 0/1 否/是
     */
    private Integer isImported;
    /**
     * 权重（品目占包的比重）保留两位小数
     */
    private String weight;
    /**
     * 上/下浮率 保留四位小数
     */
    private BigDecimal detailMultiple;
    /**
     * 实际上/下浮率 保留八位小数
     */
    private BigDecimal realDetailMultiple;

}
