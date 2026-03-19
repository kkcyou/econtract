package com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 预警规则与监控项关联关系表（new预警） Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningRuleMonitorRespVO extends WarningRuleMonitorBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21867")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
