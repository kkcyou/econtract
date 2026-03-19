package com.yaoan.module.econtract.api.gcy.order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 订单的货物信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
public class GoodsVO {
    private static final long serialVersionUID = 9112184839955649298L;
    /**
     * id主键
     */
    private String id;
    /**
     * 商品品牌名称
     */
//    @NotBlank(message = "商品品牌名称不能为空")
    @Schema(description = "商品品牌名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsBrandName;

    /**
     * 品目id
     */
//    @NotBlank(message = "品目id不能为空")
    @Schema(description = "品目id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsClassGuid;

    /**
     * 品目名称
     */
//    @NotBlank(message = "品目名称不能为空")
    @Schema(description = "品目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsClassName;

    /**
     * 商品编号
     */
//    @NotBlank(message = "商品编号不能为空")
    @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsCode;
    /**
     * 商品名称
     */
//    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsName;

    /**
     * 商品id
     */
//    @NotBlank(message = "商品id不能为空")
    @Schema(description = "商品id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsGuid;

    /**
     * 商品单价（产品/服务）
     */
//    @NotBlank(message = "商品单价不能为空")
    @Schema(description = "商品单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsOnePrice;

    /**
     * 标的图片路径
     */
//    @NotBlank(message = "标的图片路径不能为空")
    @Schema(description = "标的图片路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsPicturePath;

    /**
     * 采购数量
     */
    @NotBlank(message = "采购数量不能为空")
    @Schema(description = "采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private String qty;
    /**
     * 品目编码
     */
    @Schema(description = "品目编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsClassCode;
    /**
     * 规则型号
     */
    @Schema(description = "规则型号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regularType;
    /**
     * 订单明细唯一识别码
     */
    @Schema(description = "订单明细唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderGoodsGuid;
    /**
     * 计划明细唯一识别码
     */
    @Schema(description = "计划明细唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanBillGuid;
    /**
     * 商品总价
     */
    @Schema(description = "商品总价", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;
    /**
     * 采购分类
     */
    @Schema(description = "采购分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogType;
    /**
     * 采购分类名称
     */
    @Schema(description = "采购分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogTypeName;
//    /**
//     * 采购目录编码列表
//     */
//    @Schema(description = "采购目录编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private List<PurCatalogInfoVo> purCatalogCodeList;
//    /**
//     * 采购目录编码名称
//     */
//    @Schema(description = "采购目录编码名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purCatalogName;
    /**
     * 计量单位
     */
    @Schema(description = "计量单位", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "计量单位不能为空")
    private String unit;
    /**
     * 规格参数/服务内容
     */
    @Schema(description = "规格参数/服务内容", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "规格参数/服务内容不能为空")
    private String spec;
    /**
     * 计划采购数量
     */
    @Schema(description = "计划采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "计划采购数量")
    private Long buyPlanNum;
    /**
     * 计划采购单价
     */
    @Schema(description = "计划采购单价", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "计划采购单价")
    private BigDecimal buyPlanPrice;
    /**
     * 计划采购总价
     */
    @Schema(description = "计划采购总价", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "计划采购总价")
    private BigDecimal buyPlanTotalMoney;
    /**
     * 是否涉及进口产品(1:是,0:否)
     */
    @Schema(description = "是否涉及进口产品", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isImports;

    /**
     * 是否绿色产品
     */
    @Schema(description = "是否绿色产品", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isGreenProduct;

    /**
     * (货物类)是否环保产品(1:是,0:否)
     */
    @Schema(description = "是否环保产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer environment;
    /**
     * 是否节能产品(1:是,0:否)
     */
    private Integer efficient;

    /**
     * 采购目录编码列表
     */
    @Schema(description = "采购目录编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PurCatalogInfoVo> purCatalogCodeList;

}
