package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PaymentPlanIdListVO {
    /**
     * idList列表
     */
    @Schema(description = "idList列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "idList列表")
    private List<String> ids;
}