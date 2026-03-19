package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 19:23
 */
@Data
public class PayFinalRespVO {

    /**
     * 履约金额
     */
    @Schema(description = "履约金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private BigDecimal contractPayment;

    /**
     * 实付金额
     */
    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private BigDecimal actualPayment;

    /**
     * 实付金额
     */
    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer monthIndex;

    /**
     * 月份str
     */
    @Schema(description = "月份str", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private String monthStr;
}
