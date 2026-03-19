package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款计划
 */
@Data
public class PaymentScheduleVO {
    /**
     * 支付计划名称
     */
    private String name;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 支付期数
     */
    private Integer sort;

    /**
     * 收款人-主体id
     */
    private String payee;

    /**
     * 付款条件
     */
    private String terms;

    /**
     * 付款时间
     */
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date paymentTime;

    /**
     * 付款比例
     */
    private Double paymentRatio;

    /**
     * 付款金额
     */
    private BigDecimal amount;
    //=========阶段支付信息=========
    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 具体内容及交付结果
     */
    private String stagePaymentResult;

    /**
     * 阶段支付金额
     */
    private Double stagePaymentAmount;

    /**
     * 阶段期限（天）
     */
    private Integer stagePaymentDays;
    private String paymentDate;

    /**
     * 履约计划类型
     * {@link AmountTypeEnums}
     */
    private Integer amountType;

    /**
     * 需要验收
     * 1=需要
     * 0=不需要
     * */
    private Integer needAcceptance;

    private Integer moneyType;
}
