package com.yaoan.module.econtract.controller.admin.warning.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ContractWarningQueryOrderRespVO {
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
    private String buy_plan_code;

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

}
