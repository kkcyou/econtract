package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 预警检查配置表(new预警)更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningCfgUpdateReqVO extends WarningCfgBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22249")
    @NotNull(message = "主键不能为空")
    private String id;

}
