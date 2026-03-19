package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class TotalAmountRespVO {
    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 已收款金额
     */
    private BigDecimal receivedAmount;
    /**
     * 未收款金额
     */
    private BigDecimal unreceivedAmount ;

}
