package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 合同应收/应付情况统计图
 * @author: Pele
 * @date: 2023/11/8 21:22
 */
@Data
public class ContractAmountStatisticsRespVO {

    /**
     * 月份名称
     */
    private Integer month;

    /**
     * 应收/付金额
     */
    private BigDecimal amount;
    /**
     * 已收/付金额
     */
    private BigDecimal paidAmount;



}
