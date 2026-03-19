package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 谈判验收计划信息
 */
@Data
@XmlRootElement(name = "NegotiationAcceptancePlanInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class NegotiationAcceptancePlanInformation {
    /**
     * 预计验收日期
     */
    private Date acceptanceDate;

    /**
     * 标志性成果说明
     */
    private String resultDescription;

    /**
     * 验收方式
     */
    private String acceptanceMethod;

    /**
     * 验收标准
     */
    private String acceptanceCriteria;

    /**
     * 成本补偿费用
     */
    private Double costCompensationMoney;

    /**
     * 研发激励费用
     */
    private Double rAndDIncentivesMoney;

    /**
     * 成本补偿范围
     */
    private String costCoverageType;
}

