package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 联系信息
 */
@Data
@XmlRootElement(name = "ContactInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactInformation {
    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String telephoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 即时通讯方式
     */
    private String instantMessaging;

    /**
     * 即时通讯账号
     */
    private String msgAccount;

    /**
     * 通信地址
     */
    private String mailingAddress;

    /**
     * 邮政编码
     */
    private String postalCode;

}
