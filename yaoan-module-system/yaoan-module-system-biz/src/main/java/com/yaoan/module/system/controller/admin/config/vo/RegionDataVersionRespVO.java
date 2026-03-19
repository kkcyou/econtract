package com.yaoan.module.system.controller.admin.config.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/7 16:56
 */
@Data
public class RegionDataVersionRespVO {

    /**
     * 区划版本号（整数）
     */
    private String regionDataVersion;

    /**
     * 是否需要更新区划数据（y/n）
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     */
    private String ifUpdate;
}
