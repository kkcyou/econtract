package com.yaoan.module.econtract.api.purchasing.dto;

import lombok.Data;

@Data
public class ContractPurchaseContentPro {

    /**
     * 品目名称
     */
    private String productName;
    /**
     * 品目编码
     */
    private String productCode;
    /**
     * 采购标的
     */
    private String purchaseProject;
    /**
     * 是否进口
     */
    private String isImported;
    /**
     * 采购数量
     */
    private String purchaseAmount;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 规格型号
     */
    private String productType;
    /**
     * 单价
     */
    private String singlePrice;
    /**
     * 金额
     */
    private String sumPrice;

}
