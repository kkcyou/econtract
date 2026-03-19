package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo;


import lombok.Data;

@Data
public class CancellationFileVO {

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 附件id
     */
    private Long fileId;

    /**
     * 退回理由
     */
    private String reason;
}
