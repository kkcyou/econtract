package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 状态信息
 */
@Data
@XmlRootElement(name = "StatusInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusInformation {
    /**
     * 状态类型
     */
    private String statusType;

    /**
     * 变更信息
     */
    private ChangeInformation changeInformation;

    /**
     * 中止信息
     */
    private SuspendingInformation suspendingInformation;

    /**
     * 终止信息
     */
    private TerminationInformation terminationInformation;
}

