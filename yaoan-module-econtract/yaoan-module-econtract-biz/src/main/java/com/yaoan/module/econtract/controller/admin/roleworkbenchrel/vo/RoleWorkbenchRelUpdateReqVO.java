package com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 角色工作台关联更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleWorkbenchRelUpdateReqVO extends RoleWorkbenchRelBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4282")
    @NotNull(message = "id不能为空")
    private String id;

}
