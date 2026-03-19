package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "Contract CreateReq VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class ContractRelativeVO {
    /**
     * 签署方名称
     */
    private String signatory;

    /**
     * 签署人姓名
     */
    private String signName;
}
