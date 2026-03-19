package com.yaoan.module.econtract.controller.admin.workbenchmanage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 工作台管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WorkbenchBaseVO {

    @Schema(description = "工作台编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工作台编码不能为空")
    private String code;

    @Schema(description = "工作台名称", example = "张三")
    private String name;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "组件名称", example = "张三")
    private String componentName;

}
