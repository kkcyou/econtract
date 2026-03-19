package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import lombok.Data;

@Data
public class DashboardContractCountRespVO {

    /**
     * 履约风险合同数量
     */
    private Integer contractRiskCount;

    /**
     * 履约中合同数量
     */
    private Integer contractPerformingCount;

    /**
     * 待审批的履约审批数量
     */
    private Integer pendingApprovalCount;
}
