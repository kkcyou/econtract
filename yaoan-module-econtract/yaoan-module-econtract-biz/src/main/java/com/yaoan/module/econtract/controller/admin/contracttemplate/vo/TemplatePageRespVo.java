package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 8:21
 */
@Schema(description = "范本列表展示 VO")
@Data
public class TemplatePageRespVo {
    /**
     * 范本主键ID
     */
    @Schema(description = "范本主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 范本名称
     */
    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 范本编号
     */
    @Schema(description = "范本编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    /**
     * 范本简介（500字）
     */
    @Schema(description = "范本简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateIntro;

    /**
     * 范本分类
     */
    @Schema(description = "范本分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer templateCategoryId;


    /**
     * 范本来源(官方范本 或 标准范本)
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateSource;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String publishOrgan;

    /**
     * 范本字数
     */
    @Schema(description = "范本字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;


}
