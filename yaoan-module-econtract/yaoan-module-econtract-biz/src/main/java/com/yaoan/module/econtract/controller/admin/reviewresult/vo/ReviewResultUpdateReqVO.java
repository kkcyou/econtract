package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 智能审查结果更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewResultUpdateReqVO extends ReviewResultBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23302")
    @NotNull(message = "主键不能为空")
    private String id;

}
