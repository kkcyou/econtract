package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 15:38
 */
@Data
public class AcceptanceContractRespVO {
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同金额
     */
    private Double amount;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型
     */
    private String contractTypeName;
    /**
     * 签署日期
     */
    private Date signDate;
    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

    /**
     * 合同有效期(合并)
     */
    private String validitySum;
}
