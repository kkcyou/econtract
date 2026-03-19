package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警检查配置表(new预警) Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningCfgBaseVO {

    @Schema(description = "检查点名称", example = "赵六")
    private String name;

    @Schema(description = "模块来源")
    private String modelCode;

    @Schema(description = "模块来源名称", example = "王五")
    private String modelName;

    @Schema(description = "启用状态", example = "2")
    private String status;

}
