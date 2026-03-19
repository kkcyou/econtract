package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 14:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_batch_plan")
public class BatchPlanInfoDO extends BaseDO {
    private static final long serialVersionUID = 7074013236436766783L;

    @TableId(value = "id")
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
     * 计划来源编号
     */
    private String sourceCode;

    /**
     * 外部计划ID
     */
    private String outSitePlanId;
}
