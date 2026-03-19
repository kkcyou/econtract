package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 合同供应商
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractSupplierVo implements Serializable {

    private static final long serialVersionUID = 1880295104414050640L;
    /**
     * 合同供应商唯一识别码(自有交易平台使用，第三方平台为NULL)
     */
    @Schema(description = "合同供应商唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierGuid;
    /**
     * 供应商社会信用代码/组织机构代码/个人身份证
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierCode;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;
    /**
     * 供应商规模(参见常量【SupplierSize】定义)
     */
    @Schema(description = "供应商规模", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierSize;
    /**
     * 供应商特殊性质
     */
    @Schema(description = "供应商特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierFeatures;
    /**
     * 监管：供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLocation;
    /**
     * 融通平台：供应商所在行政区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zoneCode;
    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String foreignInvestmentType;
    /**
     * 外商国别类型
     */
    @Schema(description = "外商国别类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryType;
    /**
     * 是否为收款方(1:是,0:否)
     */
    @Schema(description = "是否为收款方", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer payee ;

    @Schema(description = "是否中标供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isBidder = 0;
    /**
     * 本供应商收款总金额(元)。当合同的多方支付方式为“不指定金额的多方支付”时此属性为NULL
     */
    @Schema(description = "本供应商收款总金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double totalMoney;
    /**
     * 供应商账户名称，如果当前供应商为收款方(1:是,0:否)则不能为空。
     */
    @Schema(description = "供应商账户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountName;
    /**
     * 收款供应商开户银行，如果当前供应商为收款方(1:是,0:否)则不能为空。
     */
    @Schema(description = "收款供应商开户银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;
    /**
     * 供应商开户银行行号
     */
    @Schema(description = "供应商开户银行行号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankNO;
    /**
     * 收款银行账号，如果当前供应商为收款方(1:是,0:否)则不能为空。
     */
    @Schema(description = "收款银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;
    /**
     * 是否代收款合同(1:是,0:否)。
     */
    @Schema(description = "是否代收款合同", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isEntrust=0;
    /**
     * 代收款账户名称，如果为代收款合同(1:是,0:否)，则不能为空。
     */
    @Schema(description = "代收款账户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String actualAccountName;
    /**
     * 代收款账户开户银行，如果为代收款合同(1:是,0:否)，则不能为空。
     */
    @Schema(description = "代收款账户开户银行", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String actualBankName;
    /**
     * 代收款账户开户银行行号，如果为代收款合同(1:是,0:否)，则不能为空。
     */
    @Schema(description = "代收款账户开户银行行号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String actualBankNO;
    /**
     * 代收款账户银行账号，如果为代收款合同(1:是,0:否)，则不能为空。
     */
    @Schema(description = "代收款账户银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String actualBankAccount;

    /**
     * 供应商角色
     */

    @Schema(description = "供应商角色", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String role;

    /**
     * 收款金额
     */
    @Schema(description = "收款金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal payAmount;
}
