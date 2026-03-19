package com.yaoan.module.econtract.controller.admin.warningitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警事项表（new预警）更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningItemUpdateReqVO extends WarningItemBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25441")
    @NotNull(message = "主键不能为空")
    private String id;

}
