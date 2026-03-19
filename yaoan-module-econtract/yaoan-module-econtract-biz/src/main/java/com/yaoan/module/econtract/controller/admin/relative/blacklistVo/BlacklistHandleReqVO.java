package com.yaoan.module.econtract.controller.admin.relative.blacklistVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "处理黑名单")
@ToString(callSuper = true)
public class BlacklistHandleReqVO extends BlacklistApplyReqVO{
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申请id不能为空")
    @NotBlank(message = "申请id不能为空")
    private String id;

}
