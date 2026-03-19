package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/6 17:11
 */
@Data
public class ModelStoreRespVO {

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

    /**
     * 富文本生成的PDF文件id
     */
    private Long rtfPdfFileId;

    /**
     * 模板分类ID
     */
    private Integer categoryId;

    /**
     * 模板分类名称
     */
    private String categoryName;

    /**
     * 合同类型ID
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 模板有效期
     */
    private String effectivePeriod;

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
     * 远端文件id
     */
    @Schema(description = "远端文件id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;

    /**
     * 审批通过时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime approveResultTime;

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
     * 是否政府采购
     */
    private Integer isGov;
    /**
     * 模板使用次数
     */
    private Long count;

}
