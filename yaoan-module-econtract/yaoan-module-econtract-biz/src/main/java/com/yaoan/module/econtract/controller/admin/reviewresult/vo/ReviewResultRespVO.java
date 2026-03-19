package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 智能审查结果 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewResultRespVO extends ReviewResultBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23302")
    private String id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
