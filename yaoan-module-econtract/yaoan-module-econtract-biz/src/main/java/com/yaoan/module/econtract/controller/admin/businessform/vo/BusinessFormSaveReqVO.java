package com.yaoan.module.econtract.controller.admin.businessform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 业务表单新增/修改 Request VO")
@Data
public class BusinessFormSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4444")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "芋艿")
    private String name;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13539")
    @NotEmpty(message = "所属业务id不能为空")
    private String businessId;

}