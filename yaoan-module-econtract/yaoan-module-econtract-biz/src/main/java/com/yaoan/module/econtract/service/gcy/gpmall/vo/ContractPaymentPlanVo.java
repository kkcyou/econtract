package com.yaoan.module.econtract.service.gcy.gpmall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * @description: 合同关联项目支付计划表
 */
@Data
public class ContractPaymentPlanVo {
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 支付金额
     */
    private Double money;
    /**
     * 计划支付日期
     */
    private String payDate;
    /**
     * 支付比例
     */
    private Double payProportion;
    /**
     * 计划支付条件
     */
    private String payTerm;
    /**
     * 支付期数
     */
    private Integer periods;

}
