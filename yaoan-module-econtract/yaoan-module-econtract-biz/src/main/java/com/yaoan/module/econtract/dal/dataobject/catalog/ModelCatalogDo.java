package com.yaoan.module.econtract.dal.dataobject.catalog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_model_catalog")
public class ModelCatalogDo extends BaseDO implements Serializable {
    private static final long serialVersionUID = 8703443217406077789L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 采购目录唯一识别码
     */
    private String catalogGuid;
    /**
     * 模板id
     */
    private String modelId;
    /**
     * 单位id
     */
    private String orgId;
    /**
     * 品目名称
     */
    private String catalogName;
    /**
     * 品目名称
     */
    private String catalogCode;
    /**
     * 采购组织形式(参见选项字典【Kind】定义)
     */
    private String kind;
    /**
     * 采购分类(参见选项字典【PurCatalogType】定义)
     * 1-货物  2-服务  3-工程
     */
    private String purCatalogType;

    /**
     * 平台码
     */
    private  String platform;

    /**
     * 模板名称
     */
    private String modelName;
//    private String catalogTypeId;
}
