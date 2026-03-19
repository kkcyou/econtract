package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 中止信息
 */
@Data
@XmlRootElement(name = "SuspendingInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuspendingInformation {
    /**
     * 中止原因
     */
    private String suspendingReason;

    /**
     * 中止起始时间
     */
    private Date suspendingStartTime;

    /**
     * 中止结束时间
     */
    private String suspendingEndingTime;

    /**
     * 中止申请人
     */
    private String suspendingProposer;


}

