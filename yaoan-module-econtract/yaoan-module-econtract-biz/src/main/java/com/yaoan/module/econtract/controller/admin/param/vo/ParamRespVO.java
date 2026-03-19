package com.yaoan.module.econtract.controller.admin.param.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用于列表查询后端返回值
 */
@Data
@Schema(description = "参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ParamRespVO extends BaseParamVO {
    @Schema(description = "参数id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "参数分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryName;

    @Schema(description = "存放参数图标的id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "存放参数图标的id")
    private Long iconId;

    @Schema(description = "minIO存放参数图标的路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "minIO存放参数图标的路径")
    private String iconPath;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "修改时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;

    @Schema(description = "创建者名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(description = "修改者", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updater;

    @Schema(description = "修改者名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updaterName;

    @Schema(description = "参数类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String typeName;

    @Schema(description = "字数长度要求名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String maxLengthName;

    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractTypeName;

    @Schema(description = "参数类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;
    /**
     * 行数 表格类型才有此字段
     */
    @Schema(description = "行数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lineNum;
    /**
     * 列数 表格类型才有此字段
     */
    @Schema(description = "列数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer columnNum;
    /**
     * 表格方向 1：橫向，2：竖向 表格类型才有此字段
     */
    @Schema(description = "表格方向", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableDirection;
    /**
     * 行高 表格类型才有此字段
     */
    @Schema(description = "行高", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lineHigh;
    /**
     * 合计行 yes：有, no:无 表格类型才有此字段
     */
    @Schema(description = "合计行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String totalLine;
    /**
     * 对齐方式 left:左对齐，right：右对齐，center：居中对齐 表格类型才有此字段
     */
    @Schema(description = "对齐方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String alignment;
    /**
     * 表格设置 表格类型才有此字段
     */
    @Schema(description = "表格设置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableSet;
    /**
     * 选项数 下拉框类型才有此字段
     */
    @Schema(description = "选项数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer optionNum;
    /**
     * 选项设置 下拉框类型才有此字段
     */
    @Schema(description = "选项设置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String optionSet;

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;
}
