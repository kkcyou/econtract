package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 变更信息
 */
@Data
@XmlRootElement(name = "ChangeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeInformation {
    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 变更时间
     */
    private Date changeTime;

    /**
     * 变更前内容
     */
    private String originalContent;

    /**
     * 变更后内容
     */
    private String content;

    /**
     * 变更申请人
     */
    private String changeProposer;
}

