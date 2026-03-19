package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息
 */
@Data
@XmlRootElement(name = "PaymentInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInformation {
    /**
     * 付款期数
     */
    private Integer periods;

    /**
     * 收款人名称
     */
    private String payee;

    /**
     * 支付金额(元)
     */
    private BigDecimal paymentAmount;

    /**
     * 计划支付日期
     */
    private Date paymentDate;

    /**
     * 付款条件
     */
    private String paymentTerms;

//    /**
//     * 款项类别
//     */
//    private String paymentType;
}

