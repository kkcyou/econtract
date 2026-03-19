package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 合同
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractVo implements Serializable {

    private static final long serialVersionUID = 4014610086529290435L;

    private String platform;
    private String contractGuid;
    private String buyPlanGuid;
    private String contractCode;
    private String contractName;
    private Double totalMoney;
    private Integer contractPayType;
    private Integer contractPayCount;
    private String multiAccountPayType;
    private String orgGuid;
    private String orgCode;
    private String orgName;
    private String orgLinkman;
    private String orgAddress;
    private String orgTelphone;
    private String orgFax;
    private String supplierName;
    private String supplierProxy;
    private String supplierAddress;
    private String supplierTelphone;
    private String supplierFax;
    private Integer supplierSex;
    /**
     * 融通平台：是否面向中小企业采购
     */
    private Integer reserveStatus;
    /**
     * 监管：是否面向中小企业采购
     */
    private Integer isReserve;
    private String supplierReserve;
    private String reserveType;
    private Double reserveMoney;
    private Integer supplierSize;
    private Integer supplierFeatures;
    /**
     * 监管：供应商所在行政区域
     */
    private String supplierLocation;
    /**
     * 融通平台：供应商所在行政区域
     */
    private String zoneCode;
    private Integer foreignInvestmentType;
    private Integer countryType;
    private String undertakingSubject;
    private Date signDate;
    private String signAddress;
    private Date performStartDate;
    private Date performEndDate;
    private String performAddress;
    private String otherInfo;
    private Integer isPerformanceMoney;
    private Double performanceMoney;
    private String performanceMoneyType;
    private Integer isRetentionMoney;
    private Double retentionMoney;
    private String retentionMoneyType;
    //自有交易平台使用，第三方平台为NULL ,项目采购为必填，电子卖场采购为NULL
    private String agentGuid;
    //项目采购为必填，电子卖场采购为NULL
    private String agentCode;
    //项目采购为必填，电子卖场采购为NULL
    private String agentName;
    private String agentType;
    private String projectGuid;
    private String projectCode;
    private String projectName;
    private String bidGuid;
    private String bidCode;
    private String bidName;
    private String unifiedDealCode;
    private Date bidOpenTime;
    private String purMethod;
    private Date bidResultDate;
    private Double bidResultMoney;
    private Integer secret;
    private Integer clauseSecret;
    private String allowMortgage="0";
    private String settlementMode;
    private Integer isAdvanceCharge;
    private Double advanceChargeMoney;
    private Integer isOnline;
    /**
     * 合同明细列表
     */
    private List<ContractBillVo> itemList;
    /**
     * 供应商列表
     */
    private List<ContractSupplierVo> supplierList;
    /**
     * 支付计划对象集合
     */
    private List<PaymentPlanDTO> paymentPlanList;
    /**
     * 附件集合
     */
    private List<ContractAttachmentVo> attachmentList;
    /**
     * 采购合同履约验收要求列表---货物类合同必填
     */
    private List<ContractAcceptanceVo> contractAcceptanceList;
}
