package com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 角色工作台关联 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class RoleWorkbenchRelBaseVO {

    @Schema(description = "角色id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20430")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @Schema(description = "角色名称", example = "李四")
    private String roleName;

    @Schema(description = "工作台id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5659")
    @NotNull(message = "工作台id不能为空")
    private String workbenchId;

    @Schema(description = "工作台名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String workbenchName;

}
