package com.yaoan.module.system.controller.admin.region.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/12 15:20
 */
@Data
public class BigRegionSimpleRespVO {
    /**
     * 数据列表
     */
    private List<RegionSimpleRespVO> regionSimpleRespVOList;
    /**
     * 区划数据版本号
     */
    private String regionDataVersion;
}