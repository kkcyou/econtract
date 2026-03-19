package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:55
 */
@Data
public class StatisticRecMoneyRespVO {

    /**
     * 签订收款总金额
     */
    private BigDecimal sumContractRecMoney;
    /**
     * 已签订且已收金额
     */
    private BigDecimal contractRecMoney;
    /**
     * 已签订且待收金额
     */
    private BigDecimal contractUnRecMoney;


}
