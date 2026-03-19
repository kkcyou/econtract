package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 合同供应商
 */
@Data
public class Suppliers {
    /**
     * 供应商身份编码
     */
    private String supplierIdentityCode;

    /**
     * 账户名称
     */
    private String supplierAccountName;

    /**
     * 开户银行
     */
    private String supplierOpeningBank;

    /**
     * 收款账号银行
     */
    private String supplierAccountNo;

    /**
     * 是否收款方
     * （1：是 0：否）
     */
    private String isPayee;

    /**
     * 本供应商收款总金额
     */
    private String paymentAccount;

    /**
     * 供应商企业规模
     */
    private String supplierScale;

}
