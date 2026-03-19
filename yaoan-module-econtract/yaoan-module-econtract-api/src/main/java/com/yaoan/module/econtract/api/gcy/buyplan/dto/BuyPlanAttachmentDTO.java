package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class BuyPlanAttachmentDTO implements Serializable {

    private static final long serialVersionUID = -7527966836454350L;


    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;
    /**
     * 附件条目唯一识别码
     */
    private String attachmentGuid;

    /**
     * 附件类型(参见采购附件类型定义)。此处该参数为：采购计划备案/核准书(BuyPlanAtt-0010) (必备附件)。
     */
    private String attachmentType;

    /**
     * 文档名称
     */
    private String fileName;

    /**
     * 附件的全路径URL
     */
    private String filePath;

}
