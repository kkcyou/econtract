package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class GpmsBuyplanParamDTO implements Serializable {

    private static final long serialVersionUID = 4017625621793957234L;

    private String platform;
    private String implementType;
    private Long startArchiveTime;
    private Long endArchiveTime;
    private String regionCode;
    private String orgGuid;
    private String orgCode;
    private String agentGuid;
    private String agentLicense;

}
