package com.yaoan.module.econtract.controller.admin.businessformfield.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 表单字段新增/修改 Request VO")
@Data
public class BusinessFormFieldSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "662")
    private String id;

    @Schema(description = "编码")
    private String fieldCode;

    @Schema(description = "名称", example = "张三")
    private String fieldName;

    @Schema(description = "所属表单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3557")
    @NotEmpty(message = "所属表单id不能为空")
    private String formId;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14245")
    @NotEmpty(message = "所属业务id不能为空")
    private String businessId;

    @Schema(description = "是否展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否展示不能为空")
    private Integer isShow;

    @Schema(description = "是否在搜索中展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否在搜索中展示不能为空")
    private Integer isSearch;

}