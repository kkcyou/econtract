package com.yaoan.module.econtract.controller.admin.workbench.vo.trend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/5 17:24
 */
@Data
public class TrendRespVO {

    /**
     * 月份index
     */
    private String monthIndex;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 数量
     */
    private Long count;

}
