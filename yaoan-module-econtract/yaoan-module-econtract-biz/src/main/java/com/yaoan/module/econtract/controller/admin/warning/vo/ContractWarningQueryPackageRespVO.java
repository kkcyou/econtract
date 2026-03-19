package com.yaoan.module.econtract.controller.admin.warning.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ContractWarningQueryPackageRespVO {
    /**
     * 包id
     */
    private String packageGuid;

    /**
     * 包名称
     */
    private String packageName;

    /**
     * 订单id
     */
    private String orderGuid;

    /**
     * 预警级别
     */
    private String warningLevel;

    /**
     * 计划编号
     */
    private String buyPlanCode;

    /**
     * 中标金额
     */
    private Double winBidAmount;

    /**
     * 中标通知书发出日期
     */
    private Date winBidTime;

    /**
     * 超期天数
     */
    private Integer overDays;

    /**
     * 采购单位名称
     */
    private String purchaseUnitName;

    /**
     * 采购单位id
     */
    private String purchaserOrgIds;

    /**
     * 是否已签订 0:否 1:是
     */
    private Integer isSign;

    private LocalDateTime overdueTime;

    private LocalDateTime signTime;

    /**
     * 合同来源
     */
    private String platform;
    /**
     * 合同来源名称
     */
    private String platformName;

}
