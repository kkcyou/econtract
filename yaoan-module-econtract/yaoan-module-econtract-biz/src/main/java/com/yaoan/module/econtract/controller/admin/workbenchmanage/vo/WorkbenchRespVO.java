package com.yaoan.module.econtract.controller.admin.workbenchmanage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkbenchRespVO extends WorkbenchBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23784")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
