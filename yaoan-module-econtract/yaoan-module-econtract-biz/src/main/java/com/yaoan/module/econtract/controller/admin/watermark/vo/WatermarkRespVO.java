package com.yaoan.module.econtract.controller.admin.watermark.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 水印管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WatermarkRespVO extends WatermarkBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3430")
    private String id;
    @Schema(description = "水印类型 自定义文字水印 = 0 业务字段 = 1", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private String typeName;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
