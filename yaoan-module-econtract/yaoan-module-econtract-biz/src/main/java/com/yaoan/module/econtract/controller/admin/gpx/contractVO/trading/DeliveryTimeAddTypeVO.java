package com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 交付时间地点方式
 */
@Data
public class DeliveryTimeAddTypeVO {
    /**
     * 交付时间
     */
    @Schema(description = "交付时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 交付地点
     */
    @Schema(description = "交付地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAdd;

    /**
     * 交付条件
     */
    @Schema(description = "交付条件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryCondition;

    /**
     * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;

    /**
     * 甲方联系人电话
     */
    @Schema(description = "甲方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerPhone;

    /**
     * 乙方指派联系人-供应商指派联系人-乙方（供应商）代表 对应 value12
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLink;

    /**
     * 乙方联系人电话
     */
    @Schema(description = "乙方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierPhone;
}
