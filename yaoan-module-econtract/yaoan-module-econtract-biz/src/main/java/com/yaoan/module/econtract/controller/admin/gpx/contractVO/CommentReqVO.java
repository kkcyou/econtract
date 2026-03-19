package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import lombok.Data;

@Data
public class CommentReqVO {
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 备注名
     */
    private String name;

    /**
     * 备注编码
     */
    private String code;
}
