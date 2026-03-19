package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:55
 */
@Data
public class PerformTaskStatisticRespVo {

    /**
     * 履约任务类型
     */
    @Schema(description = "履约任务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private String name;

    /**
     * 类型的数量
     */
    @Schema(description = "类型的数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer value;



}
