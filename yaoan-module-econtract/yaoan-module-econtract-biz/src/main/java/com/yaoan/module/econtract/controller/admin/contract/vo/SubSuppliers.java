package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同转让/分包供应商
 */
@Data
public class SubSuppliers {
    /**
     * 统一社会信用代码
     */
    private String supplierIdentityCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商规模
     */
    private String supplierScale;

    /**
     * 转让/分包金额
     */
    private BigDecimal subAmount;

    /**
     * 转让/分包内容
     */
    private String subContent;

}
