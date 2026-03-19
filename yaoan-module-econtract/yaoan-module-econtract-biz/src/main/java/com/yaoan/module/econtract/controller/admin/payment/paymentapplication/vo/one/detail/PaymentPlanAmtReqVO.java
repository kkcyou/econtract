package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentPlanAmtReqVO {

    /**
     * 付款计划id
     */
    @NotNull(message = "付款计划id不能为空")
    @Schema(description = "付款计划id")
    private String id;

    /**
     * 付款金额
     */
    @Schema(description = "付款金额")
    @NotNull(message = "付款金额不能为空")
    private BigDecimal currentPayAmount;
}
