package com.yaoan.module.econtract.controller.admin.amount.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/11 11:42
 */
@Data
public class ContractAmountYearRespVO {
    private Integer yearIndex;
    /**
     * 包含的月金额
     */
    private List<ContractAmountRespVO> monthList;
    /**
     * 排序
     */
    private Integer orderIndex;

    /**
     * 签约金额
     */
    private BigDecimal money;
}
