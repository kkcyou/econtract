package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 合同签定方信息 ---- 第五期 内蒙需求 新增
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_signatory")
public class ContractSignatoryDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -6571968024547232105L;

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
     * 甲方(公章)
     */
    private String partyACompany;

    /**
     * 甲方公司联系人名称
     */
    private String partyACompanyContactId;

    /**
     * 甲方开户名称
     */
    private String partyACompanyAccountName;

    /**
     * 甲方开户银行
     */
    private String partyACompanyBank;

    /**
     * 甲方银行账号
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
     * 甲方联系方式
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
     * 乙方(公章)
     */
    private String partyBCompany;

    /**
     * 乙方联系人名称
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
     * 乙方联系方式
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
     * 乙方外资类型
     */
    private String partyBForeignInvestmentType;
}
