package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @description: 范本库列表展示
 * @author: Pele
 * @date: 2023/9/5 17:01
 */
@Data
public class TemplateAllPermissionReqVo {

    /**
     * 范本主键ID
     */
    @Schema(description = "范本主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 范本编码
     */
    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 范本名称
     */
    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 文件页数
     */
    @Schema(description = "文件页数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageCount;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String picPath;

    /**
     * 范本分类
     */
    @Schema(description = "范本分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer templateCategoryId;

    /**
     * 范本分类名称
     */
    @Schema(description = "范本分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateCategoryName;

    /**
     * 文件id
     */
    @Schema(description = "文件id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Long remoteFileId;

    /**
     * 文件名称(带后缀)
     */
    @Schema(description = "文件名称(带后缀)", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String fileName;

    /**
     * 字数
     */
    @Schema(description = "字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime publishTime;

    /**
     * 范本来源
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private String templateSource;

    /**
     * 范本来源str
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private String templateSourceStr;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private String publishOrgan;

    /**
     * 引用数量
     */
    @Schema(description = "引用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private Integer quoteCount;

    /**
     * 个人发布时间
     */
    @Schema(description = "个人发布时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2023-08-10 16:37:41")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime publishTimeReciever;

    /**
     * 发布情况 0-未发布 1-已发布
     */
    @Schema(description = "发布情况 0-未发布 1-已发布", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer publishStatus;

    /**
     * 创建人
     */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String creator;

    /**
     * 创建人名称
     */
    @Schema(description = "创建人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String creatorName;

    /**
     * 版本
     */
    private Double version;

    /**
     * 范本上传方式 0.wps 1.富文本
     */
    private Integer uploadType;

    /**
     * 上传源文件ID
     */
    private Long sourceFileId;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * ofd文件对应ID（审批后的文件）
     */
    private Long ofdFileId;
}
