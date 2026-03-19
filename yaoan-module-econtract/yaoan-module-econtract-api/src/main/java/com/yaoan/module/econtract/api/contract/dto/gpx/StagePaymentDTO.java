package com.yaoan.module.econtract.api.contract.dto.gpx;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/27 15:47
 */
@Data
public class StagePaymentDTO {

    /**
     * 阶段名称
     */
    @Schema(description = "阶段名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String stageName;

    /**
     * 具体内容及交付结果
     */
    @Schema(description = "具体内容及交付结果", requiredMode = Schema.RequiredMode.REQUIRED)
    private String stagePaymentResult;

    /**
     * 阶段支付金额
     */
    @Schema(description = "阶段支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double stagePaymentAmount;

    /**
     * 阶段期限（天）
     */
    @Schema(description = "阶段期限（天）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stagePaymentDays;

    private String paymentDate;
}
