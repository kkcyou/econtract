package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 机构信息
 */
@Data
@XmlRootElement(name = "OrganizationInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationInfo {
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 法定代表人
     */
    private String legalRepresent;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 代理人
     */
    private String agent;

    /**
     * 所在地区
     */
    private String region;

    /**
     * 所在地区代码
     */
    private String regionCode;

    /**
     * 行政区划
     */
    private String adminDivision;

    /**
     * 行政区划代码
     */
    private String adminDivisionCode;

    /**
     * 住所
     */
    private String domicile;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 营业执照注册号
     */
    private String businessRegistrationNumber;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 网址
     */
    private String webSite;

    /**
     * 供应商规模
     */
    private String supplierSize;

    /**
     * 供应商的特殊性质
     */
    private String supplierFeatures;

    /**
     * 外商投资类型
     */
    private String foreignInvestmentType;

    /**
     * 外资国别类型
     */
    private String countryType;

    /**
     * 供应商承接主体
     */
    private String undertakingSubject;

    /**
     * 是否为收款方
     */
    private Boolean payee;

    /**
     * 是否中标供应商
     */
    private Boolean isBidder;

    /**
     * 本供应商收款总金额(元)
     */
    private BigDecimal totalMoney;

    /**
     * 是否代收款合同
     */
    private Boolean isEntrust;


}
