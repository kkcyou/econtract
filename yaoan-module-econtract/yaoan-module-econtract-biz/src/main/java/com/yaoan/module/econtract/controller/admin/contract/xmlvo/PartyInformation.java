package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 当事人信息
 */
@Data
@XmlRootElement(name = "PartyInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartyInformation {
    /**
     * 当事人类型
     */
    private String partyType;

    /**
     * 当事人标识
     */
    private String partyGuid;

    /**
     * 当事人角色
     */
    private String partyCharacter;

    /**
     * 机构信息
     */
    private OrganizationInfo organizationInfo;

    /**
     * 自然人信息
     */
    private PersionInformation personInfo;

    /**
     * 电子合同服务平台信息
     */
    private PlatformInformation platformInfo;

    /**
     * 联系信息
     */
    private ContactInformation contactInfo;

    /**
     * 银行账户信息
     */
    private BankAccountInformation accountInfo;

}
