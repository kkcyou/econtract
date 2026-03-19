package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:39
 */
@Schema(description = "查看预览范本 Response VO")
@Data
@ToString(callSuper = true)
public class ModelSimpleRespVO {

    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 范本类型
     */
    @Schema(description = "范本类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateType;

    /**
     * 范本简介（最多500个字）
     */
    @Schema(description = "范本简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateIntro;


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
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    /**
     * 范本字数
     */
    @Schema(description = "范本字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;




    @Schema(description = "文件页数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageCount;

    /**
     * 文件下载到本地的地址
     */
    @Schema(description = "文件下载到本地的地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String localPath;
}
