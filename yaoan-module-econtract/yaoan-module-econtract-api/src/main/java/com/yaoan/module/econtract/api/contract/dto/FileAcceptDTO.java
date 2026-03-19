package com.yaoan.module.econtract.api.contract.dto;


import lombok.Data;


@Data
public class FileAcceptDTO {
    /**
     * 附件名称
     */
    private String name;
    /**
     * 文件大小
     */
    private Long filesize;

    /**
     * 文件ID
     */
    private String fileId;

}
