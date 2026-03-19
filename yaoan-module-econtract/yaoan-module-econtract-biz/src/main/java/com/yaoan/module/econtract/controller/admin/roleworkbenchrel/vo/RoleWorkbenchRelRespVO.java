package com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 角色工作台关联 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleWorkbenchRelRespVO extends RoleWorkbenchRelBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4282")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
