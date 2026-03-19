package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query;

import com.yaoan.module.econtract.enums.AmountTypeEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 14:17
 */
@Data
public class AcceptancePlanRespVO {

    private String id;

//    /**
//     * 合同id
//     */
//    private String contractId;
//
//    /**
//     * 合同编码
//     */
//    private String contractCode;
//
//    /**
//     * 合同名称
//     */
//    private String contractName;

    /**
     * 期数
     */
    private Integer sort;

    /**
     * 款项比例
     */
    private BigDecimal paymentRatio;

    /**
     * 款项金额
     */
    private BigDecimal amount;

    /**
     * 结算类型
     * {@link AmountTypeEnums}
     */
    private Integer amountType;
    /**
     * 结算类型
     * {@link AmountTypeEnums}
     */
    private String amountTypeName;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private String moneyTypeName;
    /**
     * 相对方名称
     */
    private String relativeName;
    /**
     * 创建时间
     */
    private LocalDateTime updateTime;

//    /**
//     * 已结算金额
//     */
//    private BigDecimal settledAmount;
//
//    /**
//     * 已结算比例
//     */
//    private BigDecimal settledRatio;
//
//    /**
//     * 付款条件
//     */
//    private String terms;
//    /**
//     * 验收时间
//     */
//    private Date acceptanceDate;
//
//    /**
//     * 预计结款时间
//     */
//    private Date expectedPayDate;
    /**
     * 计划支付时间(预计时间)
     */
    private Date paymentTime;
    /**
     * 本次结算金额
     */
    private BigDecimal currentPayMoney;

    /**
     * 本次结算比例
     */
    private BigDecimal currentPayRatio;

    /**
     * 验收备注
     */
    private String remark;

    /**
     * 是否需要验收
     */
    private Integer needAcceptance;
}
