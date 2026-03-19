package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 合同附件信息
 */
@Data
@XmlRootElement(name = "AttachmentInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentInformation {
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 合同附件类型
     */
    private String attachmentType;

    /**
     * 附件内容
     */
    private byte[] attachment;

    /**
     * 附件全路径
     */
    private String filePath;

}

