package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


/**
 * @description: 参考模板新增VO
 * @author: Pele
 * @date: 2023/8/23 8:21
 */
@Data
public class TemplateCreateVo {

    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "编码不能为空")
    @Size(max = 100, message = "编码长度不能超过100个字符")
    private String code;

    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100个字符")
    private String name;

    /**
     * 范本上传方式 0.wps 1.富文本 2.条款
     */
    @Schema(description = "上传方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "上传方式不能为空")
    private Integer uploadType;

    /**
     * 范本内容
     */
    @Schema(description = "模板内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String content;

    /**
     * 范本分类
     */
    @Schema(description = "范本分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "分类不能为空")
    private Integer templateCategoryId;

    /**
     * 范本简介（最多500个字）
     */
    @Schema(description = "范本简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @Size(max = 500, message = "范本简介长度不能超过500个字符")
    private String templateIntro;

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

    /**
     * 范本字数
     */
    @Schema(description = "范本字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;

    /**
     * 上传的文件
     */
    @Schema(description = "上传的文件", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotNull(message = "上传的文件不能为空")
    private MultipartFile file;

    /**
     * 远端文件ID
     */
    @Schema(description = "远端文件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Long remoteFileId;

    /**
     * 版本
     */
    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Double version;

    /**
     * 条款list
     */
    private List<TermReqVO> termList;

    /**
     * 上传源文件ID
     */
    private Long sourceFileId;


    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 模板时效 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "模板时效标识")
    private Integer timeEffectModel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectEndTime;
    
}
