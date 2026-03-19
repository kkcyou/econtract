package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 项目采购表实体
 *
 * @author doujl
 * @since 2023-05-04
 */
@Data
public class ProjectPurchasingVO{

    /**
     * 项目id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 代码
     */
    private String code;

    /**
     * 采购人名称
     */
    private String purchaserName;

    /**
     * 采购人
     */
    private String purchaser;

    /**
     * 包裹数量
     */
    private Integer packageNumber;

    /**
     * 包裹名称
     */
    private String packageName;

    /**
     * 包裹预算
     */
    private Integer packageBudget;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 代理
     */
    private String agency;

    /**
     * 签订时间
     */
    private Date signTime;

    /**
     * 地址
     */
    private String address;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 起草单位
     */
    private String draftingUnit;

    /**
     * 甲方公司
     */
    private String partyACompany;

    /**
     * 甲方公司联系ID
     */
    private String partyACompanyContactId;

    /**
     * 甲方公司账户名称
     */
    private String partyACompanyAccountName;

    /**
     * 甲方公司银行
     */
    private String partyACompanyBank;

    /**
     * 甲方公司银行账户
     */
    private String partyACompanyBankAccount;

    /**
     * 甲方纳税人识别号
     */
    private String partyATaxpayerId;

    /**
     * 甲方地址
     */
    private String partyAAddress;

    /**
     * 甲方联系人
     */
    private String partyAContact;

    /**
     * 甲方传真
     */
    private String partyAFax;

    /**
     * 甲方相关方
     */
    private String partyARelative;

    /**
     * 乙方公司
     */
    private String partyBCompany;

    /**
     * 乙方公司联系ID
     */
    private String partyBCompanyContactId;

    /**
     * 乙方公司账户名称
     */
    private String partyBCompanyAccountName;

    /**
     * 乙方公司银行
     */
    private String partyBCompanyBank;

    /**
     * 乙方公司银行账户
     */
    private String partyBCompanyBankAccount;

    /**
     * 乙方纳税人识别号
     */
    private String partyBTaxpayerId;

    /**
     * 乙方地址
     */
    private String partyBAddress;

    /**
     * 乙方联系人
     */
    private String partyBContact;

    /**
     * 乙方传真
     */
    private String partyBFax;

    /**
     * 乙方外商投资类型
     */
    private String partyBForeignBusinessInvestmentType;

    /**
     * 乙方相关方
     */
    private String partyBRelative;

    /**
     * 乙方外国投资类型
     */
    private String partyBForeignInvestmentType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 合同采购内容
     */
    private List<ContractPurchaseContent> contractPurchaseContentList;
}
