package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划列表请求参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
public class PaymentPlanRespVO {
 /**
  * 支付计划id
  */
 @Schema(description = "支付计划id", requiredMode = Schema.RequiredMode.REQUIRED)
 private  String  id;
    /**
     * 支付计划名称
     */
    @Schema(description = "支付计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 计划支付时间/预计字符时间
     */
    @Schema(description = "计划支付时间/预计支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date paymentTime;
    /**
     * 付款比例
     */
    @Schema(description = "付款比例", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentRatio;
    /**
     * 付款金额
     */
    @Schema(description = "付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
    /**
     * 收款人-主体id
     */
    private String payee;
    /**
     * 收款人-主体id
     */
    private String payeeName;
    /**
     * 付款条件
     */
    @Schema(description = "付款条件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String terms;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  contractId;
   /**
    * 实际支付时间
    */
   @Schema(description = "实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
   private Date actualPayTime;
   /**
    * 状态
    * 0=未付款  1=已付款
    * {@link PaymentScheduleStatusEnums}
    */
   private Integer status;
   private String statusName;
    /**
     * 支付期数
     */
    private Integer sort;
}
