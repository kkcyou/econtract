package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;


@Data
public class GetTokenDTO<T> {

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

    private int total;
    private T data;

}
