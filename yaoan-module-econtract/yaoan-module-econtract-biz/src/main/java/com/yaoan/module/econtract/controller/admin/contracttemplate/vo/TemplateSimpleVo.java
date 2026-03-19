package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 11:41
 */
@Schema(description = "范本库制作 列表展示")
@Data
@ToString(callSuper = true)
public class TemplateSimpleVo {

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
    private String pageCount;

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
     * 字数
     */
    @Schema(description = "字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String publishOrgan;

    /**
     * 发布来源
     */
    @Schema(description = "发布来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateSource;

    /**
     * 发布来源
     */
    @Schema(description = "发布来源str", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateSourceStr;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-10 16:37:41")
    private LocalDateTime updateTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creator;

    /**
     * 创建者名称
     */
    @Schema(description = "创建者名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creatorName;

    /**
     * 更新者
     */
    @Schema(description = "更新者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String updater;

    /**
     * 更新者名称
     */
    @Schema(description = "更新者名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String updaterName;

    /**
     * 远端文件id
     */
    @Schema(description = "远端文件id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;


    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer ApproveStatus;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String ApproveStatusStr;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 被分派到任务的人
     */
    private Long assigneeId;

    /**
     * 版本
     */
    private Double version;

    /**
     * 发布情况 0-未发布 1-已发布
     */
    private Integer publishStatus;

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
     * 多租户编号
     */
    private Long tenantId;

}
