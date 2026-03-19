package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContractTypeShortReqVO {

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30947")
    private Long templateiId;

    @Schema(description = "模板名称", example = "赵六")
    private String templateName;

}
