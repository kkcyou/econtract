package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import lombok.Data;

@Data
public class DashboardContractComplateRespVO {
    /**
     * 月份
     */
    private Integer month;
    /**
     * 履约完成个数
     */
    private Integer performanceComplateCount;
    
}
