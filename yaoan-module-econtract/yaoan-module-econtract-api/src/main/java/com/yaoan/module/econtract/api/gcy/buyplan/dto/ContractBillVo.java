package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同采购明细
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractBillVo implements Serializable {

    private static final long serialVersionUID = 2608598085278867750L;
  @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractBillGuid;
  @Schema(description = "合同明细序号（不传则按照对象顺序自动分配序号）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer dOrder;
  @Schema(description = "对应采购计划明细的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanBillGuid;
  @Schema(description = "对应采购项目需求的唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String requirementGuid;
  @Schema(description = "商品/服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsName;
 @Schema(description = "品牌", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String brand;
 @Schema(description = "计量单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;
 @Schema(description = "是否进口产品采购(1:是,0:否)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isImports;
 @Schema(description = "采购目录代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purCatalogCode;
 @Schema(description = "计划采购总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planTotalPrice;
 @Schema(description = "计划采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planPurchaseNum;
 @Schema(description = "计划采购单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planPrice;
 @Schema(description = "总价/实际首购费用(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double totalPrice;
 @Schema(description = "采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double purchaseNum;
 @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double price;
 @Schema(description = "规格参数/服务内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spec;
 @Schema(description = "是否属于政府购买服务(1:是,0:否)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer govService;
 @Schema(description = "政府购买服务分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String govServiceType;
 @Schema(description = "政府购买服务指导性目录代码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String govServiceCatalogCode;
 @Schema(description = "合同明细对应的财政部统计项属性", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractBillStatisticalVo contractBillStatisticalVo;
    /**
     * 商品id
     */
    private String id;
    /**
     * 商品编号
     */
    private String goodsCode;

}
