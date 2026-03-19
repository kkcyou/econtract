package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 14:27
 */
@Data
public class BatchPlanInfo {

    private String id;
    /**
     * 批次ID
     */
    private String batchId;
    /**
     * 批次名称
     */
    private String batchName;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 计划编号
     */
    private String planCode;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 采购方式编码
     */
    private String purchaseMethodCode;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;
    /**
     * 计划预算
     */
    private BigDecimal planBudget;
    /**
     * 采购单位ID
     */
    private String purchaserId;
    /**
     * 采购单位名称
     */
    private String purchaserName;
    /**
     * 项目性质(货物/服务/工程)
     */
    private String projectType;
    /**
     * 项目性质名称
     */
    private String projectTypeName;
    /**
     * 计划区划编码
     */
    private String planZoneCode;
    /**
     * 计划区划名称
     */
    private String planZoneName;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 包ID
     */
    private String packageId;
    /**
     * 区划编码
     */
    private String zoneCode;
    /**
     * 区划名称
     */
    private String zoneName;

    /**
     * 计划明细
     */
    private List<PlanDetailInfo> planDetailInfo;


    /**
     * 计划来源编号
     */
    private String sourceCode;

    /**
     * 外部计划ID
     */
    private String outSitePlanId;
}
