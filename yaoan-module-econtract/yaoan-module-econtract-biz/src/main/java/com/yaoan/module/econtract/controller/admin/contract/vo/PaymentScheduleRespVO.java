package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PaymentScheduleRespVO {
    /**
     * 主键
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 名称
     */
    private String name;

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
    private Date paymentTime;
    /**
     * 实付时间
     */
    private Date actualPayTime;
    /**
     * 付款比例
     */
    private Double paymentRatio;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 付款状态
     */
    private Integer status;

    /**
     * 付款状态名称
     */
    private String statusName;

    /**
     * 申请状态
     */
    private Integer applyStatus;

    /**
     * 已付款金额
     */
    private BigDecimal paidAmount;
    //附件id
    private Long fileId;
    
    // 附件名称
    private String fileName;

    private String remark;

    private String confirmRemark;

    private LocalDateTime confirmTime;

    /**
     * 履约计划类型
     * {@link AmountTypeEnums}
     */
    private Integer amountType;

    /**
     * 履约计划类型
     * {@link AmountTypeEnums}
     */
    private String amountTypeName;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;

    /**
     * 款项类型名称
     */
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
    private LocalDateTime paymentRemindTime;


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
     * 需要验收
     * 1=需要
     * 0=不需要
     * */
    private Integer needAcceptance;

}
