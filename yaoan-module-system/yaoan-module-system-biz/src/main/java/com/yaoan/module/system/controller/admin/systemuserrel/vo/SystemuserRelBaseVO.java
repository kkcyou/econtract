package com.yaoan.module.system.controller.admin.systemuserrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 系统对接用户关系 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class SystemuserRelBaseVO {

    @Schema(description = "采购单位id", example = "32426")
    private String buyerOrgId;

    @Schema(description = "采购人id", example = "28279")
    private String buyerUserId;

    @Schema(description = "对应本系统用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15726")
    @NotNull(message = "对应本系统用户id不能为空")
    private Long currentUserId;

    @Schema(description = "对应本系统租户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18964")
    @NotNull(message = "对应本系统租户id不能为空")
    private Long currentTenantId;

    @Schema(description = "对应本系统部门id", example = "28504")
    private Long deptId;

    @Schema(description = "创建人名称", example = "张三")
    private String creatorName;

}
