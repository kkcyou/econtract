package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划返回类
 */
@Data
public class PayPerformancePageRespVO {
    /**
     * 支付计划id
     */
    private String id;


    /**
     * 支付计划名称
     */
    private String name;

    /**
     * 合同id
     */
    private String contractId;
    private String contractCode;
    private String contractName;


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


    /**
     * 履约状态
     */
    private Integer status;
    /**
     * 履约状态name
     */
    private String statusName;

    /**
     * 履约计划类型
     *
     */
    private Integer amountType;

    private String amountTypeName;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;
    private String moneyTypeName;
    /**
     * 是否提醒 不提醒0 提醒1
     */
    private String isRemind;
    /**
     * 提醒方式 系统消息:message
     */
    private String remindType;
    /**
     * 提醒时间
     */
    private Date paymentRemindTime;


    /**
     * 履约任务完成情况
     */
    private String finishInfo;

    // ==================  阶段支付信息 ========================

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 具体内容及交付结果
     */
    private String stagePaymentResult;



    /**
     * 阶段期限（天）
     */
    private Integer stagePaymentDays;
    /**
     * 阶段支付金额
     */
    private Double stagePaymentAmount;
    private String paymentDate;

    /**
     * 是否需要验收
     */
    private Integer needAcceptance;

    /**
     * 是否已验收
     */
    private Integer isAcceptance;

    /**
     * 可以发起申请的计划（未申请金额大于0的计划）
     * */
    private Integer ableApply;

}
