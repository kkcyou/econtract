package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:54
 */
@Data
public class StatisticCountRespVO {

    /**
     * 总合同 （ 已签订）
     */
    private Long sumContract;
    /**
     * 政府采购类 （已签订）
     */
    private Long governmentPurchase;
    /**
     * 非政府采购类（已签订）
     */
    private Long nonGovernmentPurchase;

}
