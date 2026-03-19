package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:37
 */
@Data
@Schema(description = "模板信息 Response VO")
@EqualsAndHashCode(callSuper = true)
public class ModelPageRespVO extends ModelBaseVO {

    /**
     * 模板id
     */
    private String id;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板类型
     */
    private String type;


    /**
     * 模板类型str
     */
    private String typeStr;

    private Long rtfPdfFileId;

    /**
     * 模板分类ID
     */
    private Integer categoryId;

    /**
     * 合同类型ID
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 模板分类名称
     */
    private String categoryName;

    /**
     * 模板有效期
     */
    private String effectivePeriod;


    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String ApproveStatus;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String ApproveStatusStr;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 时效模式
     */
    private Integer timeEffectModel;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 模板描述
     */
    private String modelDescription;

    /**
     * 远端文件id
     */
    @Schema(description = "远端文件id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;

    /**
     * 模板生效时间
     */
    private LocalDateTime effectStartTime;

    /**
     * 模板生效结束时间
     */
    private LocalDateTime effectEndTime;

    /**
     * 版本
     */
    private Double version;

    /**
     * 收藏 0未收藏 1已收藏
     */
    private Integer collect;

    /**
     * 生效时间内是否启用 0未启用 1启用
     */
    private Integer effectStatus;

    /**
     * 是否失效 0失效 1有效
     */
    private Integer effective;

    /**
     * 不在有效期提示语
     */
    private String effectivePeriodTips;

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
     * 是否可以撤回
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     */
    private String ifRepeal;

    /**
     * 区划编号
     */
    private String regionCode;
    /**
     * 区划名称
     */
    private String regionName;
    /**
     * 是否有编辑权限
     * 0=无，1=有
     */
    private Integer editPermission;

    /**
     * 单位id
     */
    private Integer companyId;
    /**
     * 单位名称
     */
    private String companyName;
    /**
     * 是否政府采购
     */
    private Integer isGov;
}
