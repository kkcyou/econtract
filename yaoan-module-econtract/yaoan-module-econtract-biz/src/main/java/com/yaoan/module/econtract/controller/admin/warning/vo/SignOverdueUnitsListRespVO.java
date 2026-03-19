package com.yaoan.module.econtract.controller.admin.warning.vo;
import lombok.Data;

@Data
public class SignOverdueUnitsListRespVO {
    /**
     * 采购单位名称
     */
    private String buyerOrgName;

    /**
     * 采购单位Id
     */
    private String buyerOrgId;

    /**
     * 超期已签订数量
     */
    private Long signedOverdueQuantity;
    /**
     * 超期未签订数量
     */
    private Long unSignedOverdueQuantity;


}
