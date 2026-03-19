package com.yaoan.module.system.api.region.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/18 17:34
 */
@Data
public class RegionDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 区划编号
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 父级区划编号
     */
    private Long parentId;

    /**
     * 是否是叶子节点
     */
    private Boolean leaf;

    /**
     * 区划全称
     */
    private String path;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 显示顺序
     *
     */
    private Integer sort;

    /**
     * 0 区划 1 单位
     *
     */
    private Integer tag = 0;
}
