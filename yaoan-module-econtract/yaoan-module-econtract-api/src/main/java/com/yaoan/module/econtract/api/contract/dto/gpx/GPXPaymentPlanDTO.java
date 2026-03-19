package com.yaoan.module.econtract.api.contract.dto.gpx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/27 15:48
 */
@Data
public class GPXPaymentPlanDTO {
    /**
     * 支付金额
     */
    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double money;
    /**
     * 合同金额大写
     */
    @Schema(description = "支付金额大写", requiredMode = Schema.RequiredMode.REQUIRED)
    private String shiftMoney;
    /**
     * 计划支付日期
     */
    @Schema(description = "计划支付日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 收款人名称
     */
    @Schema(description = "收款人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payee;
    /**
     * 收款人id--供应商id
     */
    @Schema(description = "收款人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payeeId;
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
}
