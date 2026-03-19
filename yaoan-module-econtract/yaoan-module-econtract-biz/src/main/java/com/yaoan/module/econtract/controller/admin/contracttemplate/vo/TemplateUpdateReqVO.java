package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/5 10:30
 */
@Data
public class TemplateUpdateReqVO {
    /**
     * 主键id
     */
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 参考模板 编码
     */
    @Schema(description = "参考模板 编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "参考模板 编码不能为空")
    @Size(max = 100, message = "参考模板 编码长度不能超过100个字符")
    private String code;

    /**
     * 参考模板 名称
     */
    @Schema(description = "参考模板 名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "参考模板 名称不能为空")
    @Size(max = 100, message = "参考模板 名称长度不能超过100个字符")
    private String name;


    /**
     * 参考模板 分类
     */
    @Schema(description = "参考模板 分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer templateCategoryId;

    /**
     * 参考模板 简介（最多500个字）
     */
    @Schema(description = "参考模板 简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @Size(max = 500, message = "参考模板 简介长度不能超过500个字符")
    private String templateIntro;

    /**
     * 参考模板 来源(官方参考模板  或 标准参考模板 )
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String templateSource;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "发布机构不能为空")
    private String publishOrgan;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
//    @NotBlank(message = "发布时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 发布时间接收器
     */
    @Schema(description = "发布时间接收器", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishTimeReciever;


    /**
     * 参考模板 字数
     */
    @Schema(description = "参考模板 字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;

    /**
     * 上传的文件
     */
    @Schema(description = "上传的文件", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
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
     * 条款说明list
     */
    @Schema(description = "条款说明list", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotEmpty(message = "条款说明list不能为空")
    private List<TermReqVO> termList;

    /**
     * 参考模板 上传方式 0.wps 1.富文本 2.条款
     */
    @Schema(description = "参考模板 上传方式 0.wps 1.富文本", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "参考模板 上传方式不能为空")
    private String uploadType;

    /**
     * 参考模板 内容
     */
    @Schema(description = "参考模板 内容", example = "1")
//    @NotBlank(message = "参考模板 内容不能为空")
    private String content;


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
