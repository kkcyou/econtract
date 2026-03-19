package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.change.IsAcceptanceEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 合同 支付计划
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_payment_schedule")
public class PaymentScheduleDO extends DeptBaseDO {
    private static final long serialVersionUID = -2184411453405862771L;

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
    private String terms;

    /**
     * 计划支付时间
     */
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
     * 实际支付时间
     */
    @Schema(description = "实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date actualPayTime;

    private String remark;

    /**
     * 状态
     {@link PaymentScheduleStatusEnums}
     */
    private Integer status;

    private String confirmRemark;

    private LocalDateTime confirmTime;
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
     * 履约计划类型
     * {@link AmountTypeEnums}
     */
    private Integer amountType;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;
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
     * 需要验收
     * 1=需要
     * 0=不需要
     * */
    private Integer needAcceptance;
    /**
     * {@link IsAcceptanceEnums}
     * 0=待发起验收
     * 1=已发起验收
     * 5=已关闭验收
     */
    private Integer isAcceptance;

}
