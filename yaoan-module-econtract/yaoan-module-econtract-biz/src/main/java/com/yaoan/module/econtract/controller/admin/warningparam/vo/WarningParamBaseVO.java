package com.yaoan.module.econtract.controller.admin.warningparam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警消息模板参数(new预警) Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningParamBaseVO {

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "合同名称")
    @NotNull(message = "参数名称不能为空")
    private String name;

    @Schema(description = "参数配置", requiredMode = Schema.RequiredMode.REQUIRED, example = "{contractName}")
    @NotNull(message = "参数配置不能为空")
    private String paramCfg;

    @Schema(description = "模块来源", example = "25462")
    private String modelId;
    /**
     * 字段名称
     */
    @Schema(description = "字段名称", example = "contractName")
    private String fieldStr;
}
