package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GpmsBuyplanExecStatusDTO implements Serializable {

    private static final long serialVersionUID = -7537192560200745425L;

    private String buyPlanGuid;
    private String platform;
    private String buyPlanExecStatus;
    private Long setTime;
    private String reason;

}
