package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:54
 */
@Data
public class ContractStatisticCountRespVO {

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
    /**
     * 政采合同类型下的合同数量
     */
    private List<ContractTypeCountVO> governmentCountVOList;
    /**
     * 非政采政采合同类型下的合同数量
     */
    private List<ContractTypeCountVO> nonGovernmentCountVOList;
    /**
     * 政采合同上涨或下降率
     */
    private Double governmentRate;
    /**
     * 政采合同上涨或下降率标识 0:持平 1:上涨 2:下降
     */
    private Integer governmentRateFlag;
    /**
     * 非政采合同上涨或下降率
     */
    private Double onGovernmentRate;
    /**
     * 非政采合同上涨或下降率标识 0:持平 1:上涨 2:下降
     */
    private Integer onGovernmentRateFlag;

}
