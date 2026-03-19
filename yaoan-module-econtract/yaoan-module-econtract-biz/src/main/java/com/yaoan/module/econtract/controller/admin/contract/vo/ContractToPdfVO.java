package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

@Data
public class ContractToPdfVO {
    /**
     * 合同名称
     */
    private String name;

    /**
     * 富文本
     */
    private String content;

    /**
     * wps文件id
     */
    private Long fileAddId;

    /**
     * pdf文件id
     * */
    private Long pdfFileId;
}
