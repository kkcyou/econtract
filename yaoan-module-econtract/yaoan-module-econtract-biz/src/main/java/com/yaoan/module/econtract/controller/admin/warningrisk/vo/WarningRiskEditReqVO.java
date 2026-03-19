package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 预警结果新增/修改 Request VO")
@Data
public class WarningRiskEditReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5786")
    @NotNull(message = "id不能为空")
    private String id;
//
//    @Schema(description = "规则编码")
//    private String warningRuleCode;
//
//    @Schema(description = "规则名称", example = "王五")
//    private String warningRuleName;
//
//    @Schema(description = "规则类型 事前提醒1 事中提醒2", example = "2")
//    private Integer warningRuleType;
//
//    @Schema(description = "规则描述", example = "随便")
//    private String warningRuleRemark;
//
//    @Schema(description = "所属业务", example = "19280")
//    private String businessTypeId;
//
//    @Schema(description = "触发字段")
//    private String warningRuleField;
//
    @Schema(description = "是否启用配置，0关闭，1启用")
    private String enable;
//
//    @Schema(description = "逾期是否业务阻断，1阻断，0不阻断")
//    private Integer isBlocked;
//
//    @Schema(description = "所属公司", example = "23789")
//    private Long companyId;

}