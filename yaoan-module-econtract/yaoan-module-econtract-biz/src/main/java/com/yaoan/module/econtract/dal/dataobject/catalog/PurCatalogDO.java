package com.yaoan.module.econtract.dal.dataobject.catalog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("ecms_jianguan_pur_catalog")
public class PurCatalogDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 8703443217406077789L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 采购目录唯一识别码
     */
    private String purCatalogGuid;
    /**
     * 财政区划唯一识别码(参见财政区划定义)
     */
    private String regionGuid;
    /**
     * 财政区划编码(参见财政区划定义)
     */
    private String regionCode;
    /**
     * 上级采购目录代码
     */
    private String purCatalogPcode;
    /**
     * 采购组织形式(参见选项字典【Kind】定义)
     */
    private String kind;
    /**
     * 采购分类(参见选项字典【PurCatalogType】定义)
     */
    private String purCatalogType;
    /**
     * 采购目录代码
     */
    private String purCatalogCode;
    /**
     * 采购目录名称
     */
    private String purCatalogName;
    /**
     * 适用计量单位
     */
    private String unit;
    /**
     * 采购目录描述
     */
    private String purCatalogDescription;
    /**
     * 是否节能产品(默认:0 无 1 可选 2 :强制)
     */
    private Integer isEfficient;
    /**
     * 是否节水产品(默认:0 无 1 可选 2 :强制)
     */
    private Integer isWaterSaving;
    /**
     * 是否环保产品(默认:0 无 1 可选 2 :强制)
     */
    private Integer isEnvironment;
}
