package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 分包供应商信息
 */
@Data
@XmlRootElement(name = "SubSupplierInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubSupplierInformation {
    /**
     * 供应商唯一识别码
     */
    private String supplierGuid;

    /**
     * 供应商统一社会信用代码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商规模
     */
    private String supplierSize;

    /**
     * 分包金额
     */
    private Double supplierMoney;

    /**
     * 主要分包内容
     */
    private String mainContent;
}

