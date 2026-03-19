package com.yaoan.module.system.api.region.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: doujl
 * @date: 2023/12/18 17:34
 */
@Data
public class MongoliaRegionDTO {
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

    /**
     * 状态 0 区划 1 单位
     */
    private Integer tag = 0;
    /**
     * 模板数
     */
    private Integer modelNumber = 0;

}
