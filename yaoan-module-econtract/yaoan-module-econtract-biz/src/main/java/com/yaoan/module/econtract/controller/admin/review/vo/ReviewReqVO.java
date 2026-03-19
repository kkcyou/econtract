package com.yaoan.module.econtract.controller.admin.review.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "Review Req VO")
@Data
public class ReviewReqVO {

    @NotNull(message = "id不能为空")
    @Schema(description = "审查清单名id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

}
