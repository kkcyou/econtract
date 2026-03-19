package com.yaoan.module.econtract.controller.admin.outward.template.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/4 10:31
 */
@Data
public class ModelApiListRespVO {
    /**
     * 模板id
     */
    private String id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板分类id
     */
    private String categoryId;

    /**
     * 模板分类名称
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 模板失效类型
     * 0 = 有时效期
     * 1 = 永久有效
     */
    private String effectType;

    /**
     * 模板时效开始时间
     */
    private String effectStartTime;

    /**
     * 模板失效接口时间
     */
    private String effectEndTime;

    /**
     * 版本
     */
    private String version;

    /**
     * 区划code
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

}



