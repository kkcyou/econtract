package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DashboardContractRiskRespVO {
    /**
     * 合同id
     */
    private String id;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同编码
     */
    private String code;

    /**
     * 计划期数
     */
    private Integer sort;
    /**
     * 结算类型  付款0 收款1
     */
    private Integer amountType;

    private String amountTypeName;
    private Integer status;
    private String statusName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date riskDate;
    
    private Integer riskDay;
}
