package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 通用信息
 */
@Data
@XmlRootElement(name = "GeneralInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralInformation {
    /**
     * 交易平台代码
     */
    private String platform;

    /**
     * 合同标识
     */
    private String contractGuid;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 采购计划标识
     */
    private String buyPlanGuid;

    /**
     * 合同编号
     */
    private String contractNO;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 总金额
     */
    private BigDecimal grossAmount;

    /**
     * 付款方式
     */
    private String paymentMethod;

    /**
     * 履行起始日期
     */
    private Date performStartDate;

    /**
     * 履行终止日期
     */
    private Date performEndDate;

    /**
     * 履行地点
     */
    private String performAddress;

    /**
     * 履行方式
     */
    private String performMethod;

    /**
     * 质量要求
     */
    private String qualityRequirement;

    /**
     * 违约责任
     */
    private String breachResponsibility;

    /**
     * 争议解决方式
     */
    private String disputeResolutionMethod;

    /**
     * 签约网址
     */
    private String signingWebsite;

    /**
     * 签约时间
     */
    private Date signingTime;

    /**
     * 分期付款次数
     */
    private Integer contractPayCount;

    /**
     * 多方支付方式
     */
    private String multiAccountPayType;

    /**
     * 面向中小企业采购
     */
    private Boolean reserveStatus;

    /**
     * 特定规模供应商采购
     */
    private String supplierReserve;

    /**
     * 预留形式
     */
    private String reserveType;

    /**
     * 中小企业预留金额
     */
    private Double reserveMoney;

    /**
     * 合同补充事宜
     */
    private String otherInfo;

    /**
     * 缴纳履约保证金
     */
    private Boolean isPerformanceMoney;

    /**
     * 履约保证金金额
     */
    private Double performanceMoney;

    /**
     * 履约保证金方式
     */
    private String performanceMoneyType;

    /**
     * 缴纳质量保证金
     */
    private Boolean isRetentionMoney;

    /**
     * 质量保证金金额
     */
    private Double retentionMoney;

    /**
     * 质量保证金方式
     */
    private String retentionMoneyType;

    /**
     * 成本补偿费用 无
     */
    private Double costCompensationMoney;

    /**
     * 研发激励费用 无
     */
    private Double rAndDIncentivesMoney;

    /**
     * 成本补偿范围 无
     */
    private String costCoverageType;

    /**
     * 首购交付日期 无
     */
    private Date deliveryDate;

    /**
     * 项目标识
     */
    private String projectGuid;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 包标识
     */
    private String bidGuid;

    /**
     * 开标时间
     */
    private Date bidOpenTime;

    /**
     * 采购方式编码
     */
    private String purMethod;

    /**
     * 中标日期
     */
    private Date bidResultDate;

    /**
     * 中标金额(元)
     */
    private Double bidResultMoney;

    /**
     * 是否涉密采购
     */
    private Boolean secret;

    /**
     * 合同涉密条款
     */
    private Boolean clauseSecret;

    /**
     * 是否融资抵押
     */
    private Boolean allowMortgage;

    /**
     * 是否预付款
     */
    private Boolean isAdvanceCharge;

    /**
     * 预付款金额
     */
    private Double advanceChargeMoney;

    /**
     * 是否电子合同 无
     */
    private Boolean isOnline;

    /**
     * 小微企业评审优惠 无
     */
    private Boolean isReserveDiscounts;

    /**
     * 明确包装要求 无
     */
    private Boolean isProductRequirement;

    /**
     * 定价方式 PackageInfoDO
     */
    private String settlementMode;

    /**
     * 分期履行要求 无
     */
    private String installmentRequirement;

    /**
     * 风险处置措施 无
     */
    private String riskSharing;

    /**
     * 知识产权归属
     */
    private String propertyOwnership;

    /**
     * 利益分配说明 无
     */
    private String benefitDistribution;

    /**
     * 售后服务方案
     */
    private String afterSaleService;

    /**
     * 风险管控措施 无
     */
    private String controlMeasures;

    /**
     * 首购评审标准 无
     */
    private String reviewCriteria;


}
