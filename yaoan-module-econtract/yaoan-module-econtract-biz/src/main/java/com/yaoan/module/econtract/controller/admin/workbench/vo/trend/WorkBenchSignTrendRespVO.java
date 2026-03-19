package com.yaoan.module.econtract.controller.admin.workbench.vo.trend;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/5 17:16
 */
@Data
public class WorkBenchSignTrendRespVO {

    /**
     * 总金额
     */
    private List<TrendRespVO> sumMoneyRespVOList;
    /**
     * 政府类金额
     */
    private List<TrendRespVO> governmentMoneyRespVOList;
    /**
     * 非政府类金额总金额
     */
    private List<TrendRespVO> nonGovernmentMoneyRespVOList;
}
