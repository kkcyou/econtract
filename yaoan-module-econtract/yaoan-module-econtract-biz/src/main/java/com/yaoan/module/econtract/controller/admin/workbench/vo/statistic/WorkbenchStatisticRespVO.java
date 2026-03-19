package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:53
 */
@Data
public class WorkbenchStatisticRespVO {
    /**
     * 统计数量
     */
    private StatisticCountRespVO countRespVO;
    /**
     * 统计付款金额
     */
    private StatisticMoneyRespVO moneyRespVO;
    /**
     * 统计收款金额
     */
    private StatisticRecMoneyRespVO recMoneyRespVO;
}
