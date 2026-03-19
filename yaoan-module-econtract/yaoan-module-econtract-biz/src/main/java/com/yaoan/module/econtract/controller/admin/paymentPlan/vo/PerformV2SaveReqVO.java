package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/23 20:00
 */
@Data
public class PerformV2SaveReqVO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

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
    @Size(max  = 500, message = "最多输入500字")
    private String terms;

    /**
     * 计划支付时间
     */
    @NotNull(message = "计划支付时间不可为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date paymentTime;

    /**
     * 付款比例
     */
    private BigDecimal paymentRatio;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 履约计划类型，付款0 收款1
     */
    private Integer amountType;

    /**
     * 实际支付时间
     */
    @Schema(description = "实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date actualPayTime;

    private String remark;

    /**
     * 状态
     * 0=未付款  1=已付款
     * {@link PaymentScheduleStatusEnums}
     */
    private Integer status;

    /**
     * 支付记录状态
     * 0=未申请  1=已申请  2：草稿箱 3：申请通过
     * {@link PaymentScheduleApplyStatusEnums}
     */
    private Integer applyStatus;

    /**
     * 支付计划名称
     */
    @Schema(description = "支付计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 已付款金额
     */
    @Schema(description = "已付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal paidAmount;

    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    @NotBlank(message = "款项类型不可为空")
    private String moneyType;
    /**
     * 是否提醒 不提醒0 提醒1
     */
    @NotNull(message = "履约计划类型不可为空")
    private Integer isRemind;
    /**
     * 提醒方式 系统消息:message
     */
    private List<String> remindType;
    /**
     * 提醒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date paymentRemindTime;



}
