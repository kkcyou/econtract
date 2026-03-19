package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "TypeStatistics VO")
@Data
@EqualsAndHashCode
public class TypeStatisticsVO {
    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private Long count;
}
