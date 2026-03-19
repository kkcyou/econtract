package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import lombok.Data;

/**
 * 批注入参
 */
@Data
public class CommentCreateReqVO {
    /**
     * 主键id
     */
    private String id;
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

    /**
     * 备注内容
     */
    private String content;
}
