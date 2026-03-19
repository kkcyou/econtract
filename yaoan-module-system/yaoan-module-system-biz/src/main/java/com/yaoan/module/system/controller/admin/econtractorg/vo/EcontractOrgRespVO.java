package com.yaoan.module.system.controller.admin.econtractorg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 电子合同单位信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EcontractOrgRespVO extends EcontractOrgBaseVO {

    @Schema(description = "单位id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12231")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
