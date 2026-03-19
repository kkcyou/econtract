package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目信息
 */
@Data
public class PatrolOrderVO {
    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderCode;
    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private String orderStatus;
    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private String orderStatusName;
    /**
     * 订单总额
     */
    @Schema(description = "订单总额")
    private BigDecimal orderTotalAmount;
    /**
     * 采购单位名称
     */
    @Schema(description = "采购单位名称")
    private String purchaserOrg;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    private String supplierName;
    /**
     * 区划名称
     */
    @Schema(description = "区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionFullName;

    /**
     * 下单时间
     */
    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime payTime;

}
