package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同支付计划
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class PaymentPlanDTO implements Serializable {

    private static final long serialVersionUID = 7934417727352234113L;
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractId;
    @Schema(description = "支付期数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer periods;
    @Schema(description = "收款人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payee;
    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double money;
    @Schema(description = "计划支付日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long payDate;
    @Schema(description = "计划支付条件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payTerm;
    @Schema(description = "支付比例/款项类别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double payProportion;

    @Schema(description = "阶段支付金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double stagePaymentAmount;


}
