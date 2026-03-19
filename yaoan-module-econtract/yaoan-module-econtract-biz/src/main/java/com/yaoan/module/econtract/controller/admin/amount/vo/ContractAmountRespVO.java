package com.yaoan.module.econtract.controller.admin.amount.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 签约金额resp
 * @author: Pele
 * @date: 2023/11/8 21:22
 */
@Data
public class ContractAmountRespVO {
    /**
     * 年份index
     */
    private Integer yearIndex;
    /**
     * 排序
     */
    private Integer orderIndex;

    /**
     * 月份index
     */
    private String monthIndex;

    /**
     * 月份名称
     */
    private String monthName;


    /**
     * 签约金额
     */
    private BigDecimal money;


}
