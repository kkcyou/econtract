package com.yaoan.module.econtract.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "demo精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoSimpleRespVO {

    @Schema(description = "demo编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "demo名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    private String name;

}
