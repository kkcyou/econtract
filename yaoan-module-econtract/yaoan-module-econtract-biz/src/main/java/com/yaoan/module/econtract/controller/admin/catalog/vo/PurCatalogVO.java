package com.yaoan.module.econtract.controller.admin.catalog.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class PurCatalogVO implements Serializable {
    private static final long serialVersionUID = 8703443217406077789L;



    /**
     * 财政区划唯一识别码(参见财政区划定义)
     */
    private String regionGuid;
    /**
     * 财政区划编码(参见财政区划定义)
     */
    private String regionCode;

    /**
     * 采购组织形式(参见选项字典【Kind】定义)
     */
    private String kind;
    /**
     * 采购分类(参见选项字典【PurCatalogType】定义)
     */
    private String purCatalogType;
    /**
     * 采购分类编码
     */
    private String purCatalogCode;

    private String purCatalogGuid;

    /**
     * 采购分类编码
     */
    private String purCatalogName;

}
