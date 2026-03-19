package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:55
 */
@Data
public class StatisticMoneyRespVO {
    /**
     * 总合同 （ 已签订）
     */
    private BigDecimal sumContractMoney;
    /**
     * 政府类金额
     */
    private GovMoneyRespVO govMoneyRespVO;
    /**
     * 非政府类金额
     */
    private NonGovMoneyRespVO nonGovMoneyRespVO;


}
