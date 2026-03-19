package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 合同采购内容信息入参
 */
@Data
public class ContractPurchaseReqVO {
    /**
     * 序号
     */
    private Long sort;

    /**
     * 品目编码
     */
    private String itemCode;

    /**
     * 品目名称
     */
    private String itemName;

    /**
     * 采购标的
     */
    private String purchaseTarget;

    /**
     * 是否进口
     */
    private Integer isImport;

    /**
     * 采购数量
     */
    private Double purchaseQuantity;

    /**
     * 计量单位
     */
    private String measuringUnit;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单价（元）
     */
    private Double unitPrice;

    /**
     * 金额（元）
     */
    private Double amount;
}
