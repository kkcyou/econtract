package com.yaoan.module.econtract.controller.admin.review.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Review PageReq VO")
@Data
public class ReviewPageReqVO extends PageParam {

    @Schema(description = "审查清单名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

}
