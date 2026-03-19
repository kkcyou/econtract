package com.yaoan.module.econtract.controller.admin.watermark.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 水印管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WatermarkUpdateReqVO extends WatermarkBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3430")
    @NotNull(message = "id不能为空")
    private String id;

}
