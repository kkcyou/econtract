package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付计划列表请求参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
public class PaymentPlanAmountRespVO {
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal noPayAmount;
    private BigDecimal ratio;

}
