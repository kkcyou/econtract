package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 单个查询响应Vo
 * @author: Pele
 * @date: 2023/8/23 3:03
 */
@Data
public class ModelSingleRespVo {


    private String id;
    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;
    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;
    @Schema(description = "模板内容")
    private String modelContent;
    private String type;
    private Long rtfPdfFileId;
    @Schema(description = "模板分类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer categoryId;
    @Schema(description = "模板分类Str", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String categoryStr;
    @Schema(description = "模板类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String contractType;
    @Schema(description = "模板类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String contractTypeStr;
    /**
     * 时效模式 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "时效模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer timeEffectModel;
    /**
     * 时效模式名称 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "时效模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String timeEffectModelStr;
    /**
     * 模板生效时间
     */
    @Schema(description = "模板生效时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime effectStartTime;
    /**
     * 模板生效结束时间
     */
    @Schema(description = "模板生效结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime effectEndTime;
    /**
     * 模板描述
     */
    @Schema(description = "模板描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "模板描述内容sample-模板描述内容sample")
    private String description;
    /**
     * 暴露给前端的唯一文件id
     */
    @Schema(description = "暴露给前端的唯一文件id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;
    /**
     * 模板相关图片的远端地址集合
     */
    @Schema(description = "模板相关图片的远端地址集合", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<String> picList;
    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
    /**
     * 范本名称
     */
    private String templateName;
    /**
     * 范本名称
     */
    private String templateId;
    /**
     * 范本文件id
     */
    private Long templateFileId;
    /**
     * 范本文件名称
     */
    private String templateFileName;

    /** 追加字段 */

    /**
     * 流程信息响应VO集合
     */
    private List<BpmProcessRespDTO> bpmProcessRespVOList;

    /**
     * wps生成的文件ID
     */
    private Long remoteFileId;

    /**
     * 版本
     */
    private Double version;

    /**
     * 历史版本
     */
    private List<ModelPageRespVO> historyList;

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
     * 文件名称
     */
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     * 参数信息
     */
    private List<ModelParamRespVO> paramRespVOList;

    private String regionName;

    private String regionCode;
    private Integer editPermission;

    /**
     * 单位id
     */
    private Long companyId;
    /**
     * 单位名称
     */
    private String companyName;
    /**
     * 是否政府采购
     */
    private Integer isGov;

    private String platform;
    private String platformName;

    /**
     * 品目名称
     */
    private List<PurCatalogRespVO> purCatalogVOList;
}
