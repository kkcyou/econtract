package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 自然人信息
 */
@Data
@XmlRootElement(name = "PersionInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersionInformation {
    /**
     * 姓名
     */
    private String personName;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件号码
     */
    private String certificateNumber;

    /**
     * 居住地址
     */
    private String residentialAddress;

}
