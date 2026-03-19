package com.yaoan.module.econtract.controller.admin.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsRespVO {

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 品目名称
     */
    private String goodsClassName;

    /**
     * 商品单价（产品/服务）
     */
    private BigDecimal goodsOnePrice;

    /**
     * 采购数量
     */
    private String qty;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 标的图片路径
     */
    private String goodsPicturePath;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单id
     */
    private String orderGoodsGuid;
}
