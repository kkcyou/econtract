package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "工作台 - 合同状态数量 response VO")
@Data
public class StatusCountVO {
    /**
     * 区分标识
     */
    @Schema(description = "区分标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer identifier;

    /**
     * 数量
     */
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long count;

    /**
     *名称
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
