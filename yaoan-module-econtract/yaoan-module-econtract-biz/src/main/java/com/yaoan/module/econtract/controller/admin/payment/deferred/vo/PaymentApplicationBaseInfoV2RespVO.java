package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 17:04
 */
@Data
public class PaymentApplicationBaseInfoV2RespVO {

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
     * 审批状态
     */
    private String flowStatus;
    /**
     * 原计划支付时间
     */
    private Date paymentTime;
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

}
