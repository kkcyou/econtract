package com.yaoan.module.econtract.dal.dataobject.contract.trading;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 为了多供应商签同一份合同的场景
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_trading_supplier")
public class TradingSupplierDO extends BaseDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
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

    /**
     * 供应商的特殊性质
     */
    @Schema(description = "供应商的特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierFeatures;

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
     * 供应商银行账号
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
     * 供应商负责人地址
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registeredAddress;

    /**
     * 供应商联系电话
     */
    @Schema(description = "供应商联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierContact;

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

    /**
     * 外资类型
     */
    @Schema(description = "外资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String investmentTypeName;

    /**
     * 供应商负责人电话
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLinkMobile;

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

    /**
     * 统一社会信用代码
     */
    private String orgCode;
}
