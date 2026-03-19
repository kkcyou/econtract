package com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 预警通知配置表（new预警） Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningNoticeCfgRespVO extends WarningNoticeCfgBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31743")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
