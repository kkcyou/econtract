package com.yaoan.module.econtract.controller.admin.gpx.vo.autofill;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: doujl
 * @date: 2024/6/3 9:45
 */
@Data
public class PlaybackReqVO {
    //合同id
    private String contractId;
    /**
     * 采购计划id
     */
    private String planId;
    /**
     * 采购包id
     */
    private String packageId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 供应商idList
     */
    private List<String> supplierIdList;
    /**
     * 计划来源编码
     */
    private String sourceCode;

    /**
     * 区域编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
}
