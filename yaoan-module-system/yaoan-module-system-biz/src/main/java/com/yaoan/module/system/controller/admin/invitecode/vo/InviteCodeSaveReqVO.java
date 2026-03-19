package com.yaoan.module.system.controller.admin.invitecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 邀请码管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InviteCodeSaveReqVO extends InviteCodeBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23156")
    private Integer id;

    @Schema(description = "租户Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;
}
