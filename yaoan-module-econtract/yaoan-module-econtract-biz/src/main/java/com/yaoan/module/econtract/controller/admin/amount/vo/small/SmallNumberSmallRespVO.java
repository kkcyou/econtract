package com.yaoan.module.econtract.controller.admin.amount.vo.small;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/9 15:52
 */
@Data
public class SmallNumberSmallRespVO {
    /**
     * 合同签约类型统计名称
     */
    @Schema(description = "合同签约类型统计名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private String name;

    /**
     * 类型总份数
     */
    @Schema(description = "类型总份数", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer number;
}
