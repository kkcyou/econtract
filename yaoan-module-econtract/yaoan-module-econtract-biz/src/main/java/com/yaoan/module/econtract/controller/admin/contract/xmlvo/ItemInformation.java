package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 条款信息
 */
@Data
@XmlRootElement(name = "ItemInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemInformation {
    /**
     * 条款编号
     */
    private String itemGuid;

    /**
     * 条款名称
     */
    private String itemName;

    /**
     * 条款描述
     */
    private String itemDescription;
}

