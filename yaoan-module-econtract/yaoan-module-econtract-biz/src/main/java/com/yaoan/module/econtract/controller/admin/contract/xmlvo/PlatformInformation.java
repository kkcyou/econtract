package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 电子合同服务平台信息
 */
@Data
@XmlRootElement(name = "PlatformInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlatformInformation {
    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 平台网址
     */
    private String platformWebsite;

    /**
     * ICP许可证编号
     */
    private String ICPNumber;

    /**
     * 其他资质
     */
    private String otherQualification;

    /**
     * 平台所属企业名称
     */
    private String companyName;

}
