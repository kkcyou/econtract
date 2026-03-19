package com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 乙方供应商信息
 */
@Data
public class TradingSupplierVO {
    /**
     * 供应商ID
     */
    @Schema(description = "供应商ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;

    /**
     * 供应商名称-公章
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;

    /**
     * 法定（或授权）代表人
     */
    @Schema(description = "法定代表人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String legalRepresentative;

    /**
     * 供应商规模
     */
    @Schema(description = "供应商规模", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierSize;
    private String supplierSizeStr;

    /**
     * 供应商的特殊性质
     */
    @Schema(description = "供应商的特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierFeatures;
    private String supplierFeaturesStr;

    /**
     * 供应商开户名
     */
    @Schema(description = "支付开户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payPlanbAccount;

    /**
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;

    /**
     * 供应商银行行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;

    /**
     * 供应商收款金额（元）---1
     */
    @Schema(description = "供应商收款金额（元）", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal supplierPayAmount;

    /**
     * 纳税人识别号---1
     */
    @Schema(description = "纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierTaxpayerNum;

    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLocation;

    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLocationName;

    /**
     * 供应商负责人地址 对应 value19
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registeredAddress;

    /**
     * 供应商负责人电话-对应 value20
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLinkMobile;

    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierFax;

    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String foreignInvestmentType;
    private String foreignInvestmentTypeStr;

    /**
     * 外资类型
     */
    @Schema(description = "外资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String investmentTypeName;
    /**
     * 外资类型
     */
    @Schema(description = "外资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryType;
    private String countryTypeStr;

    /**
     * 供应商角色
     */
    @Schema(description = "供应商角色", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    /**
     * 收款金额
     */
    @Schema(description = "收款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal payAmount;
}
