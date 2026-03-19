package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DigitalSignatureXML {
    private String signatureAlgorithm = "SHA256withRSA";
    private String signatureValue;
    private String signedBy = "慧约云科技";
    private String signedOn;
}
