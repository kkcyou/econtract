package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/9 11:44
 */
@Data
public class NonGovMoneyRespVO {

    /**
     * 非政府采购类（已签订）
     */
    private BigDecimal nonGovernmentPurchaseMoney;

    /**
     * 非政府采购类 （已签订且已付金额）
     */
    private BigDecimal nonGovernmentPayedMoney;
    /**
     * 非政府采购类 （已签订且待付金额）
     */
    private BigDecimal nonGovernmentUnpaidMoney;
}
