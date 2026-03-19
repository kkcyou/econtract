package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:31
 */
@Data
public class StatisticsAmountRespVO {
    /**
     * 合同总额
     */
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     */
    private BigDecimal payedAmount;

    /**
     * 未支付金额
     */
    private BigDecimal unpaidAmount;
}
