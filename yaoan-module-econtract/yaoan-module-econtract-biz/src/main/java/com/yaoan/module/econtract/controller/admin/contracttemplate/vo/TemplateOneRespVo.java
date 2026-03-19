package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/8 16:40
 */
@Data
public class TemplateOneRespVo {

    private String id;

    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 范本内容
     */
    @Schema(description = "模板内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String content;

    /**
     * 范本分类
     */
    @Schema(description = "范本分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer templateCategoryId;
    private String templateCategoryName;

    /**
     * 范本简介（最多500个字）
     */
    @Schema(description = "范本简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateIntro;

    /**
     * 版本
     */
    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Double version;


    /**
     * 合同类型
     */
    private String contractType;
    private String contractTypeName;

    /**
     * 模板时效 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "模板时效标识")
    private Integer timeEffectModel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectEndTime;

    private List<TermOneRespVO> termOneRespVOS;
    /**
     * 范本来源(官方范本 或 标准范本)
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "范本来源不能为空")
    private String templateSource;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "发布机构不能为空")
    private String publishOrgan;



    @Schema(description = "发布时间接收器", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
//    @NotBlank(message = "发布时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}
