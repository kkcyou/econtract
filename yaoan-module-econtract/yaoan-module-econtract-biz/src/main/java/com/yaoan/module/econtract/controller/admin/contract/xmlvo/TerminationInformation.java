package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 终止信息
 */
@Data
@XmlRootElement(name = "TerminationInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminationInformation {
    /**
     * 终止原因
     */
    private String terminationReason;

    /**
     * 终止时间
     */
    private Date terminationTime;

    /**
     * 终止申请人
     */
    private String terminationProposer;
}

