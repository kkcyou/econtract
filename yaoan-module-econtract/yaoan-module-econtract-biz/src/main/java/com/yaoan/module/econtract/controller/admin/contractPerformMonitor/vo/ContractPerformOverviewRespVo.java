package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/31 22:57
 */
@Data
@NoArgsConstructor
public class ContractPerformOverviewRespVo {
    /**
     * 编码
     */
    private Integer code;
    /**
     * 统计事项
     */
    @Schema(description = "统计事项", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 统计的数量
     */
    @Schema(description = "统计的数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;

    public ContractPerformOverviewRespVo(int code, String name, Integer number) {
        this.code = code;
        this.name = name;
        this.number = number;
    }
}
