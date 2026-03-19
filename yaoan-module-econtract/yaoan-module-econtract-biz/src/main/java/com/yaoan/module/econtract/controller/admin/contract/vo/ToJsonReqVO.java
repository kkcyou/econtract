package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "智能填写-提取信息入参")
@Data
public class ToJsonReqVO {
    /**
     * 模板id
     */
    @Schema(description = "模板id")
    String templateId;

    /**
     * 文件id
     */
    @Schema(description = "文件id")
    Long fileId;

    /**
     * 上传文件路径
     */
    @Schema(description = "上传文件路径")
    String filePath;
}
