package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同基本信息
 */
@Data
public class ContractBaseInfo {
    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 采购类型
     */
    private String purchaseType;

    /**
     * 付款方式
     */
    private String paymentMethod;

    /**
     * 分期次数
     */
    private Integer installmentNum;

    /**
     * 采购单位名称
     */
    private String purchaserUnit;

    /**
     * 采购单位联系人
     */
    private String purchaserLiaison;

    /**
     * 采购单位归属区划
     */
    private String purchaserArea;

    /**
     * 采购人地址
     */
    private String purchaserAddress;

    /**
     * 采购人联系方式
     */
    private String purchaserPhone;

    /**
     * 采购人传真
     */
    private String purchaserFax;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商规模
     */
    private String supplierScale;

    /**
     * 供应商联系人
     */
    private String supplierLiaison;

    /**
     * 供应商归属区划
     */
    private String supplierArea;

    /**
     * 供应商地址
     */
    private String supplierAddress;

    /**
     * 供应商联系方式
     */
    private String supplierPhone;

    /**
     * 供应商传真
     */
    private String supplierFax;

    /**
     * 合同签订日期
     */
    private String signingDate;

    /**
     * 合同签订地点
     */
    private String signingPlace;

    /**
     * 合同履约开始日期
     */
    private String performanceStartDate;

    /**
     * 合同履约结束日期
     */
    private String performanceEndDate;

    /**
     * 履约地点
     */
    private String performancePlace;

    /**
     * 合同其他补充事项
     */
    private String supplementMater;

    /**
     * 是否缴纳履约保证金
     * 1：是 0：否
     */
    private Integer isPerformanceDeposit;

    /**
     * 履约保证金金额
     */
    private BigDecimal performanceDepositAmount;

    /**
     * 是否缴纳质量保证金
     * 1：是 0否
     */
    private Integer isQualityDeposit;

    /**
     * 质量保证金金额
     */
    private BigDecimal qualityDepositAmount;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 是否预付款
     * 1：是 0：否
     */
    private Integer isPrepayment;

    /**
     * 预付款金额
     */
    private BigDecimal prepaymentAmount;

    /**
     * 分期履行要求
     */
    private String installmentRequirement;

    /**
     * 成本不成和风险分担约定
     */
    private String costRecoveryConvention;

    /**
     * 知识产权归属
     */
    private String intellectualPropertyOwner;

    /**
     * 保密内容
     */
    private String secrecyContent;

    /**
     * 违约责任
     */
    private String defaultLiability;


}
