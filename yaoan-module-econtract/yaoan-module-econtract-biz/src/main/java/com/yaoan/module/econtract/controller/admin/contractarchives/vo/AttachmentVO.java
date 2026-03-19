package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AttachmentVO {

    @Schema(description = "附件id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    private Long fileId;

    @Schema(description = "附件名称")
    private String name;

    @Schema(description = "新增标识0:否、1：是")
    private Integer isAdd;

}