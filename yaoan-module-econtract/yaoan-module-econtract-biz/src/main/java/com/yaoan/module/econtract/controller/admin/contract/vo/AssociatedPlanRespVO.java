package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/6 11:26
 */
@Data
public class AssociatedPlanRespVO {
    /**
     * 计划id
     */
    @NotBlank(message = "计划id不能为空")
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空")
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanName;

    /**
     * 计划编号
     */
    @NotBlank(message = "计划编号不能为空")
    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanCode;

    /**
     * 计划金额
     */
    @NotNull(message = "计划金额不能为空施")
    @Schema(description = "计划金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal buyPlanMoney;

    /**
     * 可签约金额
     */
    @Schema(description = "可签约金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal signMoney;
}
