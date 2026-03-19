package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

/**
 * @author doujiale
 */
@Data
public class RestResponseDTO<T> {

    private Boolean success;

    private String requestId;

    private String code;

    private String message;

    private String msg;

    private T data;
}
