package com.yaoan.module.system.controller.admin.invitecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 邀请码管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InviteCodeRespVO extends InviteCodeBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23156")
    private Integer id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
