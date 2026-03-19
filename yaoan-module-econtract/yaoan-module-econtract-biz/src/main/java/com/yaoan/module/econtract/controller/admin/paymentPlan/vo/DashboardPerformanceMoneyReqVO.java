package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DashboardPerformanceMoneyReqVO {

    @NotNull
    private int year;
    @NotNull
    private int month;
}
