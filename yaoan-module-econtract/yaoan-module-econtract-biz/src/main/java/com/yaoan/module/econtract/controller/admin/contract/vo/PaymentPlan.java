package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付计划
 */
@Data
public class PaymentPlan {
    /**
     * 付款期
     */
    private String paymentPeriod;

    /**
     * 收款人名称
     */
    private String payee;

    /**
     * 支付比例
     */
    private BigDecimal paymentPercent;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 计划支付日期
     */
    private String planPaymentDateDesc;

    /**
     * 付款条件
     */
    private String paymentCondition;


}
