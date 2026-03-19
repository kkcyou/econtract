package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ContractDraft {
    private String data;
    private String appId;
    private String accessToken;
}
