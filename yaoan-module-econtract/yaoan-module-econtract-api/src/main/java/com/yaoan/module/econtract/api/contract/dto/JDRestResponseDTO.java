package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

/**
 * @author doujiale
 */
@Data
public class JDRestResponseDTO<T> {

    private Boolean success;


    private String resultCode;

    private String resultMsg;


    private String result;
}
