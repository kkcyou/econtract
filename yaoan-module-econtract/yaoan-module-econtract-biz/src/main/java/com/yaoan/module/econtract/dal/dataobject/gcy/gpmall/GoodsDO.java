package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @description: 订单的货物信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_goods")
public class GoodsDO extends BaseDO {
    private static final long serialVersionUID = 9112184839955649298L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 商品品牌名称
     */
    private String goodsBrandName;

    /**
     * 品目id
     */
    private String goodsClassGuid;

    /**
     * 品目名称
     */
    private String goodsClassName;

    /**
     * 商品编号
     */
    private String goodsCode;
    /**
     * 商品名称
     */
    private String goodsName;


    /**
     * 商品id
     */
    private String goodsGuid;

    /**
     * 商品单价（产品/服务）
     */
    private BigDecimal goodsOnePrice;

    /**
     * 标的图片路径
     */
    private String goodsPicturePath;

    /**
     * 采购数量
     */
    private String qty;
    /**
     * 品目编码
     */
    private String goodsClassCode;
    /**
     * 规则型号
     */
    private String regularType;

    /**
     * 订单id
     */
    private String orderId;


    //======2024-02-18 新增========

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
//    @Schema(description = "采购分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purCatalogTypeName;
    /**
     * 采购目录编码
     */
    @Schema(description = "采购目录编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogCode;
    /**
     * 采购目录编码名称
     */
//    @Schema(description = "采购目录编码名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purCatalogName;
    /**
     * 计量单位
     */
    @Schema(description = "计量单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计量单位不能为空")
    private String unit;
    /**
     * 规格参数/服务内容
     */
    @Schema(description = "规格参数/服务内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "规格参数/服务内容不能为空")
    private String goodsSpecification;
    /**
     * 计划采购数量
     */
    @Schema(description = "计划采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计划采购数量")
    private Double buyPlanNum;
    /**
     * 计划采购单价
     */
    @Schema(description = "计划采购单价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计划采购单价")
    private BigDecimal buyPlanPrice;
    /**
     * 计划采购总价
     */
    @Schema(description = "计划采购总价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计划采购总价")
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
     * 合同id
     */
    private String contractId;
}
