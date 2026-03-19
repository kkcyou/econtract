package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

@Data
public class ContractProcessDTO {
    private  Long userId;
    private String contractId;
    private  String reason;
    // 操作人名称
    private String updaterStr;
}
