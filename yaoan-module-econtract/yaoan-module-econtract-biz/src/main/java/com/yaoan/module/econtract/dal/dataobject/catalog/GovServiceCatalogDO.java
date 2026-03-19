package com.yaoan.module.econtract.dal.dataobject.catalog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_jianguan_gov_service_catalog")
public class GovServiceCatalogDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 8703443217406077789L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     *采购目录代码
     */
    private String purCatalogCode;
    /**
     *采购目录名称
     */
    private String purCatalogName;
    /**
     *上级购买服务目录唯一识别码
     */
    private String govServiceCatalogPGuid;
    /**
     *服务领域编码
     */
    private String govServiceRangeCode;
    /**
     *服务领域名称
     */
    private String govServiceRangeName;
    /**
     *政府购买服务分类
     */
    private String govServiceType;
    /**
     *购买服务品目唯一识别码
     */
    private String govServiceDivideGuid;
    /**
     *购买服务目录唯一识别码
     */
    private String govServiceCatalogGuid;
    /**
     *政府购买服务指导性目录代码
     */
    private String govServiceCatalogCode;
    /**
     *政府购买服务指导性目录名称
     */
    private String govServiceCatalogName;

}
