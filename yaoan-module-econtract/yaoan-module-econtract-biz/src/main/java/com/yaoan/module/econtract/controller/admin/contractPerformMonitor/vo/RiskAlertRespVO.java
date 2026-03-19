package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 12:12
 */
@Data
public class RiskAlertRespVO {
    /**
     * 履约风险来源
     * */
    @Schema(description = "履约风险来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private String riskResource;

    /**
     * 履约风险原因
     * */
    @Schema(description = "履约风险原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private String riskReason;

    /**
     * 履约时间
     * */
    @Schema(description = "履约时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-08 15:16:10")
    private Date performTime;

}
