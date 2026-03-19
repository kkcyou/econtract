package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 20:17
 */
@Data
public class PaymentDeferredInfoRespVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 本次付款金额（元）
     */
    private BigDecimal currentPayAmount;

    /**
     * 本期付款金额（大写）
     */
    private String currentPayAmountCapitalize;

    /**
     * 本期付款后付款进度
     */
    private BigDecimal payRate;

    /**
     * 结算方式
     */
    private String settlementMethod;

    /**
     * 付款编号
     */
    private String paymentApplyCode;

    /**
     * 申请人id
     */
    private String applicantId;

    /**
     * 申请人名字
     */
    private String applicantName;

    /**
     * 本期付款后累计已付
     */
    private BigDecimal payedAmount;

    /**
     * 本期付款后剩余应付
     */
    private BigDecimal unpaidAmount;

    /**
     * 付款类型
     */
    private Integer paymentType;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 审批状态
     */
    private String flowStatus;

    /**
     * 延期付款日期
     */
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */

    private String reason;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 实付时间
     */
    private Date payDate;

    /**
     * 本次付款后累计实付金额(历史数据，持久化的)
     */
    private BigDecimal afterPayedAmount;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 原计划付款日期
     */
    private Date originalPaymentDate;
    /**
     * 计划id
     */
    private String planId;
}
