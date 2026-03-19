package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 医疗备案专用合同
 * @author: Pele
 * @date: 2024/6/26 17:48
 */
@Data
public class ContractMVO implements Serializable {


    private static final long serialVersionUID = -6634809479018038679L;
    private String platform;
    private String contractGuid;
    /**
     * 医疗专用
     */
    private String buyplanGuid;
    private String contractCode;
    private String contractName;
    private Double totalMoney;
    private String contractPayType;
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
    private Integer isReserve;
    private String supplierReserve;
    private String reserveType;
    private Double reserveMoney;
    private String supplierSize;
    private String supplierFeatures;
    private String supplierLocation;
    private String foreignInvestmentType;
    private String countryType;
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
    private String agentGuid;
    private String agentCode;
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
    private String allowMortgage = "0";
    private Integer isAdvanceCharge;
    private Double advanceChargeMoney;
    private Integer isOnline;
    private List<ContractBillVo> itemList;
    private List<ContractSupplierVo> supplierList;
    private List<PaymentPlanDTO> paymentPlanList;
    private List<ContractAttachmentVo> attachmentList;
}


