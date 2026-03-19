package com.yaoan.module.econtract.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Schema(description = "demo更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DemoUpdateReqVO extends DemoBaseVO {

    @Schema(description = "demo编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "demo编号不能为空")
    private String id;

}
