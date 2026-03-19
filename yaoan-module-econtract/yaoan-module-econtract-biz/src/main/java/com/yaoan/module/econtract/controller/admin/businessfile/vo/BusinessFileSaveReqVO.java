package com.yaoan.module.econtract.controller.admin.businessfile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 业务数据和附件关联关系新增/修改 Request VO")
@Data
public class BusinessFileSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30671")
    private String id;

    @Schema(description = "业务表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26313")
    @NotEmpty(message = "业务表id不能为空")
    private String businessId;

    @Schema(description = "附件id", example = "6398")
    private Long fileId;

    @Schema(description = "附件名称", example = "测试合同")
    private String fileName;
}