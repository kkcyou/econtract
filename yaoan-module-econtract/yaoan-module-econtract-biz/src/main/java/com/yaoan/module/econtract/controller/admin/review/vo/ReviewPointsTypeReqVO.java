package com.yaoan.module.econtract.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "ReviewPointsTypeReqVO")
@Data
public class ReviewPointsTypeReqVO {

    @Schema(description = "审查点名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

}
