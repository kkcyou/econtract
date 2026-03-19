package com.yaoan.module.econtract.controller.admin.supervise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ContractPayeeInfoVO {
    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商id不能为空")
    private String supplierId;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;
    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商所在区域不能为空")
    private String supplierLocation;
    /**
     * 供应商规模
     */
    @Schema(description = "供应商规模", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商规模不能为空")
    private Integer supplierSize;
    /**
     * 供应商特殊性质
     */
    @Schema(description = "供应商特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商特殊性质不能为空")
    private Integer supplierFeatures;
    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "外商投资类型不能为空")
    private Integer foreignInvestmentType;

    /**
     * 收款账户名称
     */
    @Schema(description = "收款账户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收款账户名称不能为空")
    private String payeeAccountName;
    /**
     * 本供应商收款金额
     */
    @Schema(description = "本供应商收款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double supplierPayeeMoney;
    /**
     * 开户银行
     */
    @Schema(description = "开户银行", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "开户银行不能为空")
    private String bankName;
    /**
     * 银行账号
     */
    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "银行账号不能为空")
    private String bankAccount;

    /**
     * 是否使用代收款账户，0：否，1：是
     */
    @Schema(description = "是否使用代收款账户", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用代收款账户不能为空")
    private Integer isUseAgencyAccount;
    /**
     * 实际银行账号
     */
    @Schema(description = "实际银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualBankAccount;
    /**
     * 实际开户银行
     */
    @Schema(description = "实际开户银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualBankName;
    /**
     * 实际收款账户
     */
    @Schema(description = "实际收款账户", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualPayeeAccountName;
    /**
     * 外资国别类型
     */
    @Schema(description = "外资国别类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryType;


}
