package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购明细
 */
@Data
public class PurchaseDetail {
    /**
     * 商品/服务名称
     */
    private String productName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 规格型号技术参数
     */
    private String parameters;

    /**
     * 计量单位
     */
    private String saleUnit;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 采购数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 规格参数/服务内容
     */
    private String serviceScope;

}
