package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;


@Data
public class SppGPTGetTokenDTO {

    /**
     * 接入平台id
     *
     */
    private String appId;

    /**
     * 提示内容
     *
     */
    private Integer timestamp;

    /**
     * 参数签名
     *
     */
    private String sign;

}
