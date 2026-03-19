package com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警规则与监控项关联关系表（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningRuleMonitorBaseVO {

    @Schema(description = "规则id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19868")
    @NotNull(message = "规则id不能为空")
    private String ruleId;

    @Schema(description = "监控项id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23073")
    @NotNull(message = "监控项id不能为空")
    private String monitorId;

}
