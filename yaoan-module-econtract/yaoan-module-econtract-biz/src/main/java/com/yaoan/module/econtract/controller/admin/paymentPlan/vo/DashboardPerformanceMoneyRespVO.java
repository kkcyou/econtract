package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import lombok.Data;

@Data
public class DashboardPerformanceMoneyRespVO {

    /**
     * 已付款金额
     */
    private Integer paidMoney;

    /**
     * 待付款金额
     */
    private Integer unpaidMoney;

    /**
     * 付款计划总金额
     */
    private Integer totalPayPlanMoney;


    /**
     * 已收款金额
     */
    private Integer collectedMoney;

    /**
     * 待收款金额
     */
    private Integer unCollectedMoney;

    /**
     * 收款计划总金额
     */
    private Integer totalCollectPlanMoney;
}
