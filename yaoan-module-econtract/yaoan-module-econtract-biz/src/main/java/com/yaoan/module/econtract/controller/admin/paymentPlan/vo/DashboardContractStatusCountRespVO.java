package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import lombok.Data;

@Data
public class DashboardContractStatusCountRespVO {

    /**
     * 合同总数量
     */
    private Integer totalContractCount;

    /**
     * 履约关闭合同数量
     */
    private Integer contractColseCount;

    /**
     * 履约风险合同数量
     */
    private Integer contractRiskCount;

    /**
     * 履约完成合同数量
     */
    private Integer contractPerformanceComplateCount;

    /**
     * 未履约合同数量 --包括履约中
     */
    private Integer contractUnStartCount;

    //履约争议合同数量 DISPUTE
    private Integer contractRiskDisputeCount;
    //履约暂停合同数量
    private Integer contractRiskPauseCount;
    
    //履约延期合同数量
    private Integer contractRiskExtensionCount;
    
    //履约逾期合同数量OVERDUE
    private Integer contractRiskOverdueCount;
}
