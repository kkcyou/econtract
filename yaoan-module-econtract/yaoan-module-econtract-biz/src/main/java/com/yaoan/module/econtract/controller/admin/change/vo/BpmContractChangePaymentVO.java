package com.yaoan.module.econtract.controller.admin.change.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public class BpmContractChangePaymentVO  {
    /**
     * 主键
     */
    private String id;

    /**
     * 变动id
     */
    private String changeId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 原计划id
     */
    private String paymentId;

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
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
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
     * 支付计划名称
     */
    @Schema(description = "支付计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 状态
     * TO_PUBLISH(-1,"待发布"),
     * TO_DO(0, "未开始"),
     * DOING(1, "执行中"),
     * DONE(2, "已完成"),
     * CLOSE(5, "已关闭"),
     * {@link PaymentScheduleStatusEnums}
     */
    private Integer status;

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

    private String confirmRemark;

    private LocalDateTime confirmTime;

    /**
     * 提醒时间
     */
    private LocalDateTime paymentRemindTime;

}
