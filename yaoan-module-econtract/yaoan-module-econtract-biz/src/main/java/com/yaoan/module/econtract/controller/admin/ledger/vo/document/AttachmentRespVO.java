package com.yaoan.module.econtract.controller.admin.ledger.vo.document;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:53
 */
@Data
public class AttachmentRespVO {
    /**
     * 无后缀名称
     */
    private String name;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径URL （预览用）
     */
    private String fileUrl;

    /**
     * 文件下载id （下载用）
     */
    private Long fileId;
}
