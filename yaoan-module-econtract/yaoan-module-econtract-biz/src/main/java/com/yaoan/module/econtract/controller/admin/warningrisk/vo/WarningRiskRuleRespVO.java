package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 风险规则返回VO，该VO包含不同规则的所有字段
 */

@Schema(description = "预警管理后台 -  预警规则详情返回VO")
@Data
public class WarningRiskRuleRespVO {
    @Schema(description = "规则id")
    private Long id;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "规则类型")
    private Integer ruleType;

    @Schema(description = "规则类型名称")
    private String ruleTypeName;

    @Schema(description = "规则内容")
    private String ruleContent;

    @Schema(description = "规则状态")
    private Integer ruleStatus;

    @Schema(description = "规则状态名称")
    private String ruleStatusName;

    @Schema(description = "创建时间")
    private Long createTime;




}
