package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_accessory")
public class OrderAccessoryDO extends BaseDO {

    private static final long serialVersionUID = -7579839107575973365L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 配件价格GUID
     */
    private String accessoryPriceGuid;


    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 单价
     */
    private BigDecimal finalPrice;

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
    private Integer qty;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 配件金额
     */
    private BigDecimal accessoryTotalMoney;

    /**
     * 配件类型名称
     */
    private String catalogName;
    /**
     * 配件类型编码
     */
    private String catalogCode;
}
