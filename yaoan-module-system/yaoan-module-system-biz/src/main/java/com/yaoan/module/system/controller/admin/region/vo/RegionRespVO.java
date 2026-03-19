package com.yaoan.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class RegionRespVO {

    /**
     * 区划id
     */
    @Schema(description = "区划编号")
    private String regionGuid;

    /**
     * 区划父ID
     */
    @Schema(description = "区划父ID", example = "1024")
    private String regionParentGuid;

    /**
     * 区划编码
     */
    @Schema(description = "区划编码", example = "1024")
    private String regionCode;

    /**
     * 监管区域编码
     */
    @Schema(description = "监管区域编码", example = "1024")
    private String zoneCode;

    /**
     * 区划名称
     */
    @Schema(description = "区划名称", example = "1024")
    private String regionName;

    /**
     * 区划简称
     */
    @Schema(description = "区划简称", example = "1024")
    private String shortName;

    /**
     * 展示名称
     */
    @Schema(description = "展示名称", example = "1024")
    private String showName;

    /**
     * 接口编码（交互编号）
     */
    @Schema(description = "接口编码", example = "1024")
    private String interfaceCode;

    /**
     * 区划完整名称
     */
    @Schema(description = "区划完整名称", example = "1024")
    private String regionFullName;

    /**
     * 排序
     */

    @Schema(description = "显示顺序", example = "1024")
    private Integer sort;
}
