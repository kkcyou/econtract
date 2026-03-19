package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 巡检合同列表返回vo
 *
 * @author zhc
 * @since 2024-04-02
 */
@Data
public class PatrolContractPageRespVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;
    /**
     * 合同类型：项目采购、框架协议采购、电子卖场
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;
    /**
     * 合同类型名称：项目采购、框架协议采购、电子卖场
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractTypeName;
    /**
     * 项目名称
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectName;
    /**
     * 项目编号
     */
    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectCode;

    /**
     * 下单时间/中标/成交通知书发出时间
     */
    @Schema(description = "中标(成交)日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date bidResultDate;
    /**
     * 采购人名称
     */
    @Schema(description = "采购人(采购单位)名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierName;
    /***
     * 中标(成交)金额(元)
     */
    @Schema(description = "中标(成交)金额(元)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double bidResultMoney;
    /**
     * 超期时间(天)
     */
    @Schema(description = "超期时间(天)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long OverDays;
    /**
     * 是否签订
     */
    @Schema(description = "是否签订", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isSign;
    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date contractSignTime;
    /**
     * 签订截止时间
     */
    @Schema(description = "签订截止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime contractSignEndTime;

    /**
     * 巡检状态
     */
    @Schema(description = "巡检状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String statusName;
    /***
     * 采购方式
     */
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purMethod;
    /**
     * 订单编号
     */

    @Schema(description = "订单编号")
    private String orderCode;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额")
    private BigDecimal totalMoney;
    /**
     * 合同是否备案 0：未备案  1：已备案
     */
    @Schema(description = "合同是否备案", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isExport;
    /**
     * 区划编号
     */
    @Schema(description = "区划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

}
