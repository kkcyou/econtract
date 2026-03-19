package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 项目信息
 */
@Data
public class SuperviseProjectVO {
    /**
     * 项目开标时间
     */
    @JsonFormat( pattern = "yyyy-MM-dd")
    @Schema(description = "项目开标时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date bidOpenTime;
    /**
     * 中标（成交）时间
     */
    @Schema(description = "中标（成交）时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "中标（成交）时间不能为空")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date bidResultDate;
    /**
     * 采购实施形式
     */
    @Schema(description = "采购实施形式", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "采购实施形式不能为空")
    private String implement;
//    /**
//     * 采购实施形式名称
//     */
//    @Schema(description = "采购实施形式名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "采购实施形式名称不能为空")
//    private String implementName;
    /**
     * 标项编码
     */
    @Schema(description = "标项编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemCode;
    /**
     * 标项名称
     */
    @Schema(description = "标项名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemName;
    /**
     * 采购组织形式
     */
    @Schema(description = "采购组织形式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购组织形式不能为空")
    private String kind;
//    /**
//     * 采购组织形式名称
//     */
//    @Schema(description = "采购组织形式名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "采购组织形式名称不能为空")
//    private String kindName;
    /**
     * 项目编码
     */
    @Schema(description = "项目编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目编码不能为空")
    private String projectCode;
    /**
     * 项目负责人
     */
    @Schema(description = "项目负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目负责人不能为空")
    private String projectLeader;
    /**
     * 项目负责人联系电话
     */
    @Schema(description = "项目负责人联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目负责人联系电话不能为空")
    private String projectLeaderTel;
    /**
     * 项目经办人
     */
    @Schema(description = "项目经办人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目经办人不能为空")
    private String projectManager;
    /**
     * 项目经办人联系电话
     */
    @Schema(description = "项目经办人联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目经办人联系电话不能为空")
    private String projectManagerTel;
    /**
     * 项目名称
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目名称不能为空")
    private String projectName;
    /**
     * 采购分类
     */
    @Schema(description = "采购分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购分类不能为空")
    private String purCatalogType;
    /**
     * 采购分类名称
     */
    @Schema(description = "采购分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "采购分类名称不能为空")
    private String purCatalogTypeName;
    /**
     * 采购方式
     */
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购方式不能为空")
    private String purMethod;
    /**
     * 采购方式名称
     */
    @Schema(description = "采购方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @NotBlank(message = "采购方式名称不能为空")
    private String purMethodName;
}
