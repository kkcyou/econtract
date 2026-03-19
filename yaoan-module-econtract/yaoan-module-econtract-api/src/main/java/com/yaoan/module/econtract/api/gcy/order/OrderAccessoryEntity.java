package com.yaoan.module.econtract.api.gcy.order;

import lombok.Data;

/**
 * @description: 订单的配件信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
public class OrderAccessoryEntity {

    private static final long serialVersionUID = -7579839107575973365L;
    /**
     * id主键
     */
    private String id;

    /**
     * 配件价格GUID
     */
    private String accessoryPriceGuid;

    /**
     * 配件类型
     */
    private int accessoryType;

    /**
     * 目录编码
     */
    private String catalogCode;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 最终价格
     */
    private double finalPrice;

    /**
     * 商品品牌GUID
     */
    private String goodsBrandGuid;

    /**
     * 商品品牌名称
     */
    private String goodsBrandName;

    /**
     * 商品分类GUID
     */
    private String goodsClassGuid;

    /**
     * 商品分类名称
     */
    private String goodsClassName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品保持GUID
     */
    private String goodsKeepGuid;

    /**
     * 配件名称
     */
    private String name;

    /**
     * 订单配件GUID
     */
    private String orderAccessoryGuid;

    /**
     * 订单商品GUID
     */
    private String orderGoodsGuid;

    /**
     * 图片路径
     */
    private String picturePath;

    /**
     * 数量
     */
    private int qty;


}
