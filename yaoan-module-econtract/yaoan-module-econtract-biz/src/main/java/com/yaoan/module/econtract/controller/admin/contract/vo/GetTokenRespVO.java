package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;


@Data
public class GetTokenRespVO {

    /**
     * 错误码
     *
     */
    private Integer code;

    /**
     * 提示内容
     *
     */
    private String msg;

    /**
     * token
     *
     */
    private String entity;

}
