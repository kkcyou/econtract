package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 银行账户信息
 */
@Data
@XmlRootElement(name = "BankAccountInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountInformation {
    /**
     * 账户名称
     */
    private String bankAccountName;

    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 开户银行地址
     */
    private String bankAddress;

    /**
     * 银行账号
     */
    private String bankAccountNumber;

    /**
     * 开户银行行号
     */
    private String bankNO;


}

