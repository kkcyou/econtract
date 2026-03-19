package com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同采购内容
 */
@Data
public class PurchaseVO {
    /**
     * 包明细id
     */
    private String detailId;
    /**
     * 合同包号
     */
    private Integer packageNumber;
    /**
     * 计划明细id
     */
    private String planDetailId;
    /**
     * 计划明细单价
     */
    private BigDecimal planPrice;
    /**
     * 计划明细数量
     */
    private BigDecimal planNumber;
    /**
     * 计划明细总价
     */
    private BigDecimal planTotalPrice;
    /**
     * 品目编号
     */
    private String catalogueCode;
    /**
     * 品目名称
     */
    private String catalogueName;
    /**
     * 明细名称
     */
    private String deatilName;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 技术参数
     */
    private String detailParameter;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 数量
     */
    private BigDecimal detailNumber;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 货物计量单位
     */
    private String unit;
    /**
     * 排序序号
     */
    private Integer detailSequence;
    /**
     * 否涉及进口产品 0/1 否/是
     */
    private Integer isImported;
    /**
     * 最高限价
     */
    private BigDecimal limitAmount;
    /**
     * 是否核心产品 0/1 否/是
     */
    private Integer isCoreProduct;
    /**
     * 区划编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 服务指导性目录编号
     */
    private String serviceCode;
    /**
     * 服务指导性目录名称
     */
    private String serviceName;
    /**
     * 服务指导性目录GUID
     */
    private String serviceGuid;
    /**
     * 服务年限
     */
    private String serviceLife;
    /**
     * 项目类型 (0为合同制,1为凭单制)
     */
    private String projectTypeCode;
    /**
     * 项目类型名称
     */
    private String projectTypeName;
    /**
     * 预算金额
     */
    private String budgetMoney;
    /**
     * 资金来源编码
     */
    private String sourceFundsCode;
    /**
     * 资金来源名称
     */
    private String sourceFunds;
    /**
     * 是否集采目录单 （0为否，1为是）
     */
    private String centralizedPurchaseCatalog;
    /**
     * 是否超过限额标准单（0为否，1为是）
     */
    private String standardSheet;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否节能产品
     */
    private Integer efficient;
    /**
     * 是否环保产品
     */
    private Integer environment;
    /**
     * 所属行业编码
     */
    private String industryCode;
    /**
     * 所属行业名称
     */
    private String industryName;

    /**
     * 包id
     */
    private String packageGuid;

    /**
     * 工程范围
     */
    private String workScope;

    /**
     * 选中 1已选 0未选
     */
    private Integer selected;
}
