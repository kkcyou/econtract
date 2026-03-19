package com.yaoan.module.econtract.controller.admin.contract.vo;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "ContractVO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractEndXmlVO {
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;
    private Double amount;
    /**
     * 甲方名称
     */
    private String partAName;
    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 支付计划
     */
    private List<PaymentPlanXML> paymentPlan;
    private DigitalSignatureXML digitalSignature;
}
