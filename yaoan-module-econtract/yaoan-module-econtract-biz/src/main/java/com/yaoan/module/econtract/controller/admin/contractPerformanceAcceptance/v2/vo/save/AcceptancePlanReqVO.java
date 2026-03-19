package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 12:04
 */
@Data
public class AcceptancePlanReqVO {

    private String planId;

    /**
     * 本次结算金额
     */
    private BigDecimal currentPayMoney;

    /**
     * 本次结算比例
     */
    private BigDecimal currentPayRatio;
}
