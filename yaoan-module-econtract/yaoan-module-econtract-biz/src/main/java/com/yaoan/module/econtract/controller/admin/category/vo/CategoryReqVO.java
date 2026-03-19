package com.yaoan.module.econtract.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * 编辑使用
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分类")
public class CategoryReqVO extends CategoryBaseVO {
    @Schema(description = "分类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "分类id不能为空")
    private Integer id;
}
