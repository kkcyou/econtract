package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 安全信息
 */
@Data
@XmlRootElement(name = "SecurityInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SecurityInformation {
    /**
     * 加密算法
     */
    private String algorithm = "SHA256withRSA";

    /**
     * 加密数据
     */
    private String encryptedData;

    /**
     * 数字证书
     */
    private String digitalCertificate;

    /**
     * 电子签名
     */
    private String electronicSignature = "慧约云科技";

    /**
     * 签名时间戳
     */
    private String timestamp;
}

