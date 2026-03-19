package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/10 23:44
 */
@Data
public class PayableExcelVO {
    /**
     * 月份index
     */
    private String monthIndex;

    /**
     * 月份名称
     */
    private String monthName;

    /**
     * 履约金额
     */
    private Integer performMoney;

    /**
     * 实付金额
     */
    private Integer actualMoney;


}
