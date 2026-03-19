package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/20 16:57
 */
@Data
public class StatisticsAmountV2RespVO {
    /**
     * 收款应收
     */
    private BigDecimal receiptAmount;
    /**
     * 收款已付
     */
    private BigDecimal doneReceiptAmount;
    /**
     * 收款待支付
     */
    private BigDecimal toReceiptAmount;

    /**
     * 应付款
     */

    private BigDecimal payAmount;
    /**
     * 已付款
     */
    private BigDecimal donePayAmount;
    /**
     * 待支付
     */
    private BigDecimal toDoPayAmount;

    /**
     * 采购合同金额
     */
    private BigDecimal purchaseAmount;
    /**
     * 工程
     */
    private BigDecimal engPurchaseAmount;
    /**
     * 服务
     */
    private BigDecimal servPurchaseAmount;
    /**
     * 货物
     */
    private BigDecimal goodsPurchaseAmount;


}
