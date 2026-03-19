package com.yaoan.module.system.controller.admin.systemuserrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 系统对接用户关系 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemuserRelRespVO extends SystemuserRelBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12120")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
