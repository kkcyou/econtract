package com.yaoan.module.econtract.controller.admin.warning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同备案时效性详情-项目信息vo
 *
 * @author zhc
 * @since 2024-07-18
 */
@Data
public class ContractBakProjectVO {

    /**
     * 采购包名称
     */
    @Schema(description = "采购包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String packageName;
    /**
     * 采购包号
     */
    @Schema(description = "采购包号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer packageNumber;
    /**
     * 计划编号
     */
    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planCode;
    /**
     * 采购人名称
     */
    @Schema(description = "采购人(采购单位)名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;

    /**
     * 中标价格
     */
    @Schema(description = "中标价格", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double winBidAmount;
    /**
     * 组织形式名称
     */
    @Schema(description = "组织形式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgMethodName;
    /**
     * 组织形式编码
     */
    @Schema(description = "组织形式编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgMethodCode;
    /**
     * 采购方式編码
     *
     */
    @Schema(description = "采购方式編码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethodCode;
    /**
     * 采购方式名称
     */
    @Schema(description = "采购方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethodName;
    /**
     * 实施形式编码：101一般项目采购，201框架协议采购，301批量采购归集
     */
    @Schema(description = "实施形式编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String implement;
    /**
     * 实施形式名称：101一般项目采购，201框架协议采购，301批量采购归集
     */
    @Schema(description = "实施形式名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String implementName;
    /**
     * 采购单位联系人
     */
    @Schema(description = "采购单位联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;
    /**
     * 采购单位联系人电话
     */
    @Schema(description = "采购单位联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLinkMobile;


}
