package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JacksonXmlRootElement(localName = "Contract")
@Data
public class ContractXMLVO {
    @JacksonXmlProperty(localName = "BasicInfo")
    private BasicInfo basicInfo;
    @Data
    public static class BasicInfo {
        @JacksonXmlProperty(localName = "ContractID")
        private String contractID;

        @JacksonXmlProperty(localName = "Name")
        private String name;
        @JacksonXmlProperty(localName = "SignAddress")
        private String SignAddress;

        @JacksonXmlProperty(localName = "SignDate")
        private String signDate;

        @JacksonXmlProperty(localName = "Amount")
        private BigDecimal amount;

        @JacksonXmlProperty(localName = "Currency")
        private String currency;
    }



    @JacksonXmlProperty(localName = "Parties")
    private Parties parties;
    @Data
    public static class Parties {
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "Party")
            private List<Party> partyList;
        }
    @Data
    public static class Party {
        @JacksonXmlProperty(isAttribute = true)
        private String role;

        @JacksonXmlProperty(localName = "Name")
        private String name;

        @JacksonXmlProperty(localName = "Address")
        private String address;

        @JacksonXmlProperty(localName = "Contact")
        private String contact;
        @JacksonXmlProperty(localName = "Proxy")
        private String proxy;

        @JacksonXmlProperty(localName = "Phone")
        private String phone;
        @JacksonXmlProperty(localName = "BankName")
        private String bankName;
        @JacksonXmlProperty(localName = "BankAccount")
        private String bankAccount;
    }


    @JacksonXmlProperty(localName = "PerformancePlan")
    private PerformancePlan performancePlan;
    @Data
    public static class PerformancePlan {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "Phase")
        private List<Phase> phaseList;
    }
    @Data
    public static class Phase {
        @JacksonXmlProperty(localName = "PhaseID")
        private String phaseID;
        @JacksonXmlProperty(localName = "Description")
        private String description;
        @JacksonXmlProperty(localName = "DueDate")
        private String dueDate;
        @JacksonXmlProperty(localName = "Amount")
        private Double amount;
        @JacksonXmlProperty(localName = "Currency")
        private String currency;
    }
    @JacksonXmlProperty(localName = "Signature")
    private Signature signature;
    @Data
    public static class Signature {
        @JacksonXmlProperty(localName = "SignatureAlgorithm")
        private String signatureAlgorithm;
    }



}
