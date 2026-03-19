package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Schema(description = "TemplateQuote PageResp VO")
@Data
public class TemplateQuotePageRespVO {

    /**
     * 单位名称
     */
    @Schema(description = "单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    /**
     * 新增模板名称
     */
    @Schema(description = "新增模板名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modelName;

    /**
     * 模板id
     */
    @Schema(description = "模板id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modelId;
    /**
     * 模板编码
     */
    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modelCode;

    /**
     * 范本编码
     */
    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateCode;

    /**
     * 引用时间
     */
    @Schema(description = "引用时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 模板版本
     */
    @Schema(description = "模板版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double modelVersion;

    /**
     * 状态
     */
    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 范本版本
     */
    @Schema(description = "范本版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double templateVersion;

}