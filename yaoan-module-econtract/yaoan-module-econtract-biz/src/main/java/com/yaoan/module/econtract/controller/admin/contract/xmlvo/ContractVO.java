package com.yaoan.module.econtract.controller.admin.contract.xmlvo;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
/**
 * 电子合同基础信息
 */
@Data
@XmlRootElement(name = "ContractVO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractVO {
    /**
     * 通用信息
     */
    private GeneralInformation generalInformation;

    /**
     * 当事人信息列表
     */
    private PartyInformations partyInformations;

    /**
     * 标的信息列表
     */
    private ContractObjectInformations contractObjectInformations;

    /**
     * 支付信息列表
     */
    private PaymentInformations paymentInformations;

    /**
     * 条款信息列表
     */
    private ItemInformations itemInformations;

    /**
     * 分包供应商信息列表
     */
    private SubSupplierInformations subSupplierInformations;

    /**
     * 履约验收要求信息列表
     */
    private AcceptanceInformations acceptanceInformations;

    /**
     * 谈判验收计划信息列表
     */
    private NegotiationAcceptancePlanInformations negotiationAcceptancePlanInformations;

    /**
     * 项目经理信息列表
     */
    private ProjectManagerInformations projectManagerInformations;

    /**
     * 合同附件信息列表
     */
    private AttachmentInformations attachmentInformations;

    /**
     * 状态信息
     */
    private StatusInformation statusInformation;

    /**
     * 安全信息
     */
    private SecurityInformation securityInformation;


    @Data
    public static class PartyInformations{
        private List<PartyInformation> partyInformation;
    }
    @Data
    public static class ContractObjectInformations{
        private List<ContractObjectInformation> contractObjectInformation;
    }
    @Data
    public static class PaymentInformations{
        private List<PaymentInformation> paymentInformation;
    }
    @Data
    public static class ItemInformations{
        private List<ItemInformation> itemInformation;
    }
    @Data
    public static class SubSupplierInformations{
        private List<SubSupplierInformation> subSupplierInformation;
    }
    @Data
    public static class AcceptanceInformations{
        private List<AcceptanceInformation> acceptanceInformation;
    }
    @Data
    public static class NegotiationAcceptancePlanInformations{
        private List<NegotiationAcceptancePlanInformation> negotiationAcceptancePlanInformation;
    }
    @Data
    public static class ProjectManagerInformations{
        private List<ProjectManagerInformation> projectManagerInformation;
    }
    @Data
    public static class AttachmentInformations{
        private List<AttachmentInformation> attachmentInformation;
    }
}
