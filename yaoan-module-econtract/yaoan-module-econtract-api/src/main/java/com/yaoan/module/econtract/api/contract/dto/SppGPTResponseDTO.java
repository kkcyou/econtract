package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

import java.util.HashMap;


@Data
public class SppGPTResponseDTO {

    /**
     * 返回对象
     */
    private HashMap data;

    /**
     * 错误码
     */
    private Integer errCode;

    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 成功标识
     */
    private Boolean success;


}
