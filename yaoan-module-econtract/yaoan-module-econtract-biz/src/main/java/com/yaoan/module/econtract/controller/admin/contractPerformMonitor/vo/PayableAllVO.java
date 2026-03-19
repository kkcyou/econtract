package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 17:06
 */
@Data
public class PayableAllVO {

    @Schema(description = "履约日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Date contractDate;

    @Schema(description = "履约金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private BigDecimal contractPayment;

    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private BigDecimal actualPayment;

    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer monthIndex;
}
