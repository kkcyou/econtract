package com.yaoan.module.econtract.api.contract;

import lombok.Data;

/**
 * 供应商用户 Response DTO
 *
 * @author zhc
 */


@Data

public class SupplyFromHLJDTO {

    /**
     * 用户ID
     */
    private String id;
    /**
     * 供应商名称中文
     */
    private String supplyCn;
    /**
     * 统一社会信用代码
     */
    private String orgCode;
    /**
     * 开户银行账号
     */
    private String bankAccount;
    /**
     * 开户银行
     */
    private String bankName;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 企业类型
     */
    private String companyType;
    /**
     * 经济性质（字典）
     */
    private String ecokindcode;
    /**
     * 企业认定行业划分（下拉字典）
     */
    private String hyhf;
    /**
     * 地址
     */
    private String addr;
    /**
     * 传真号码
     */
    private String fax;
    /**
     * 企业电话
     */
    private String tel;
    /**
     * 供应商类型
     */
    private String type;
    /**
     * 法人
     */
    private String legalPerson;
    /**
     * 法人电话
     */
    private String legalTel;
    /**
     * 法人地址
     */
    private String legalAddr;

    /**
     * 联系人地址
     */
    private String personAddr;
    /**
     * 联系人电话
     */
    private String personMobile;
    private String personTel;
    /**
     * 联系人姓名
     */
    private String personName;
    /**
     * 工商注册地址（地区字典+地址）
     */
    private String regAddr;
    /**
     * 企业规模（大、中、小、微、其他）(提示：填写''营业收入''和''从业人员''信息后，系统会自动做出企业规模认定。)0 大型企业\n4 微型企业\n1 中型企业\n2
     * 小型企业\n3 其他
     */
    private String unitScopeCode;
    /**
     * 供应商区划编码
     */
    private String reginCode;


}
