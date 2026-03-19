package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 智能审查结果 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ReviewResultBaseVO {

    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13430")
    @NotNull(message = "合同id不能为空")
    private String contractId;

    @Schema(description = "审查结果（0=通过，1=低风险，2=中风险，3=高风险）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审查结果（0=通过，1=低风险，2=中风险，3=高风险）不能为空")
    private Integer result;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "标题不能为空")
    private String title;

    @Schema(description = "风险等级（1=低风险，2=中风险，3=高风险）")
    private Integer riskLevel;
    @Schema(description = "风险等级（1=低风险，2=中风险，3=高风险）")
    private String riskLevelName;
    @Schema(description = "风险提示")
    private String riskWarning;

    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "版本不能为空")
    private Long version;

}
