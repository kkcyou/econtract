package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "智能填写-返回值")
@Data
public class JsonRespVO {
    /**
     * 名称
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String name;

    /**
     * 编码
     */
    @Schema(description = "编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String code;

    /**
     * 值
     */
    @Schema(description = "值", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String value;
}
