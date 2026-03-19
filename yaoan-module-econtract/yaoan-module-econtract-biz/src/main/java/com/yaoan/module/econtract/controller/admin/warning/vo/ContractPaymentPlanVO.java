package com.yaoan.module.econtract.controller.admin.warning.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ContractPaymentPlanVO {
    /**
     * 支付金额
     */
    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;
    /**
     * 计划支付日期
     */
    @Schema(description = "计划支付日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date payDate;
    /**
     * 支付比例
     */
    @Schema(description = "支付比例", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double payProportion;
    /**
     * 计划支付条件
     */
    @Schema(description = "计划支付条件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payTerm;
    /**
     * 支付期数
     */
    @Schema(description = "支付期数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer periods;

    /**
     * 是否已提交发票
     */
    @Schema(description = "是否已提交发票", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSubmitInvoice;
    /**
     * 是否已确认
     */
    @Schema(description = "是否已确认", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isConfirm;

}
