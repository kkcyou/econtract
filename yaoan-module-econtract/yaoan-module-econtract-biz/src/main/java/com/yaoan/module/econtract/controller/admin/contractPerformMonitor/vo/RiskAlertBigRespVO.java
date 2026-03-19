package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 13:51
 */
@Data
public class RiskAlertBigRespVO {

    /**
     * 履约风险来源
     * */
    @Schema(description = "履约风险来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private  Date bigPerformTime;

    /**
     * 履约风险来源
     * */
    @Schema(description = "履约风险来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private  List<RiskAlertRespVO> respVOList;
}
