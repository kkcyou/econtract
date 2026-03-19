package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:33
 */
@Data
public class PaymentApplicationReqVO {

    /**
     * 申请主键
     */
    private String id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 本次付款金额（元）
     */
    @TableField("current_pay_amount")
    private BigDecimal currentPayAmount;

    /**
     * 本期付款金额（大写）
     */
    @TableField("current_pay_amount_capitalize")
    private String currentPayAmountCapitalize;

    /**
     * 本期付款后付款进度
     */
    @TableField("pay_rate")
    private BigDecimal payRate;

    /**
     * 结算方式
     */
    @TableField("settlement_method")
    private String settlementMethod;

    /**
     * 付款编号
     */
    @TableField("payment_apply_code")
    private String paymentApplyCode;

    /**
     * 申请人id
     */
    @TableField("applicant_id")
    private String applicantId;

    /**
     * 申请人名字
     */
    @TableField("applicant_name")
    private String applicantName;

    /**
     * 本期付款后累计已付
     */
    @TableField("payed_amount")
    private BigDecimal payedAmount;

    /**
     * 本期付款后剩余应付
     */
    @TableField("unpaid_amount")
    private BigDecimal unpaidAmount;

    /**
     * 付款类型
     */
    @TableField("payment_type")
    private Integer paymentType;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;


    /**
     * 审批状态
     */
    @TableField("flow_status")
    private String flowStatus;

    /**
     * 延期付款日期
     */
    @TableField("deferred_payment_date")
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */

    private String reason;

    /**
     * 申请时间
     */
    @TableField("apply_time")
    private LocalDateTime applyTime;

    /**
     * 付款计划 list
     */
    private List<String> planIdList;

    /**
     * 文件id
     */
    private Long fileId;



}
