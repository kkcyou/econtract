package com.yaoan.module.econtract.controller.admin.catalog.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GovServiceCatalogVO implements Serializable {
    private static final long serialVersionUID = 8703443217406077789L;

    private String id;
    /**
     * 上级购买服务目录唯一识别码
     */
    private String govServiceCatalogPGuid;
    /**
     * 政府购买服务分类
     */
    private String govServiceType;

    /**
     * 政府购买服务指导性目录代码
     */
    private String govServiceCatalogCode;

    /**
     * 服务领域编码
     */
    private String govServiceRangeCode;
    /**
     * 服务领域名称
     */
    private String govServiceRangeName;

    /**
     * 购买服务目录唯一识别码
     */
    private String govServiceCatalogGuid;

    /**
     * 政府购买服务指导性目录名称
     */
    private String govServiceCatalogName;
    private List<GovServiceDivideVO> govServiceDivideVO;
}
