package com.yaoan.module.econtract.controller.admin.gpx.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/31 13:55
 */
@Data
public class PlanRespVO {

    private String planId;
    /**
     * 计划编号
     */
    private String planCode;

    private String planName;

    /**
     * 计划金额
     */
    private BigDecimal planAmount;

    /**
     * 可签约金额
     */
    private BigDecimal toSignAmount;

    /**
     * 计划来源
     */
    private String sourceCode;


//
//    /**
//     * 供应商列表
//     */
//    private List<SupplierRespVO> supplierRespVOs;
}
