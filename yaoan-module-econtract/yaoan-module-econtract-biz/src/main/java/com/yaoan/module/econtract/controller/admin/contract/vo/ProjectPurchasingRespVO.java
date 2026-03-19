package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 项目采购推送数据
 */
@Data
public class ProjectPurchasingRespVO {
    /**
     * 主键
     */
    private String id;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
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
     * 采购包号
     */
    private String packageNumber;

    /**
     * 采购包名
     */
    private String packageName;

    /**
     * 采购包预算
     */
    private Long packageBudget;

    /**
     * 采购方式，公开招标，邀请招标竞争性谈判，单一来源采购，询价，国务院政府采购监督管理部门认定的其他采购方式
     */
    private String payType;

    /**
     * 项目类型，工程，货物，服务
     */
    private String projectType;

    /**
     * 合同金额
     */
    private Integer amount;

    /**
     * 代理机构
     */
    private String agency;

    /**
     * 签订日期
     */
    private String signTime;

    /**
     * 所在地
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
     * 删除标识
     */
    private Long isDeleted;

    //------------------------签定方信息---------------------

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
     * 模板id 1货物,2服务,3工程
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板文件id
     */
    private Long templateFileId;

    /**
     * 合同采购内容
     */
    private List<ContractPurchaseContent> contractPurchaseContentList;
}
