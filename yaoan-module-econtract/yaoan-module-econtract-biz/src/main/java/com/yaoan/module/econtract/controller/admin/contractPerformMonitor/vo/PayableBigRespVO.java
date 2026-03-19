package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 17:22
 */
@Data
public class PayableBigRespVO {
    /**
     * 月份index
     */
    private String monthIndex;

    /**
     * 月份名称
     */
    private String monthName;

    /**
     * 实付金额
     */
    private Integer actualMoney;

    /**
     * 履约金额
     */
    private Integer performMoney;


}
