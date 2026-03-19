package com.yaoan.module.econtract.controller.admin.warningparam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警消息模板参数(new预警)更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningParamUpdateReqVO extends WarningParamBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17285")
    @NotNull(message = "主键不能为空")
    private String id;

}
