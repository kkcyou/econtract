package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SuperviseGoodsVO {
    //purCatalogCode purCatalogName | goodsName  -->拼接成采购标的内容

    /**
     * 商品/服务名称(采购内容)
     */
    @Schema(description = "商品/服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品/服务名称不能为空")
    private String goodsName;
    /**
     * 采购品目代码
     */
    @Schema(description = "采购品目代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购品目代码不能为空")
    private String purCatalogCode;
    /**
     * 采购品目名称
     */
    @Schema(description = "采购品目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购品目名称不能为空")
    private String purCatalogName;


    /**
     * 实际购买金额
     */
    @Schema(description = "实际购买金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际购买金额不能为空")
    private Double actualBuyMoney;
    /**
     * 实际购买数量
     */
    @Schema(description = " 实际购买数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = " 实际购买数量不能为空")
    private Integer actualBuyNum;
    /**
     * 实际购买单价
     */
    @Schema(description = "实际购买单价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际购买单价不能为空")
    private Double actualBuyPrice;
    /**
     * 实际购买单位
     */
    @Schema(description = "实际购买单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "实际购买单位不能为空")
    private String actualBuyUnit;
    /**
     * (实际)一般公共预算资金
     */
    @Schema(description = "(实际)一般公共预算资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney1;
    /**
     * (实际)政府性基金预算
     */
    @Schema(description = "(实际)政府性基金预算", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney2;
    /**
     * (实际)其他资金
     */
    @Schema(description = "(实际)其他资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney3;
    /**
     * (实际)非财政性资金
     */
    @Schema(description = "(实际)非财政性资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney4;
    /**
     * (实际)非同级财政拨款
     */
    @Schema(description = "(实际)非同级财政拨款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney5;
    /**
     * (实际)其他单位资金
     */
    @Schema(description = "(实际)其他单位资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractMoney6;
    /**
     * 实际支付总金额
     */
    @Schema(description = "实际支付总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double contractTotalMoney;
    /**
     * 额外节能条目
     */
    @Schema(description = "额外节能条目", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer extraEnergySaving;
    /**
     * 明细项唯一识别码(全局唯一)
     */
    @Schema(description = "明细项唯一识别码(全局唯一)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "明细项唯一识别码不能为空")
    private String billGuid;
    /**
     * 是否节能产品
     */
    @Schema(description = "是否节能产品", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isEnergySavingProducts;
    /**
     * 是否绿色产品-环保
     */
    @Schema(description = "是否绿色产品", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isGreenProduct;
    /**
     * 是否进口
     */
    @Schema(description = "是否进口", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isImport;
    /**
     * 是否节水产品
     */
    @Schema(description = "是否节水产品", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isWaterSavingProduct;
    /**
     * 计划采购金额
     */
    @Schema(description = "计划采购金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划采购金额不能为空")
    private Double planBuyMoney;
    /**
     * 计划采购数量
     */
    @Schema(description = "计划采购数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划采购数量不能为空")
    private Integer planBuyNum;
    /**
     * 计划采购单价
     */
    @Schema(description = "计划采购单价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划采购单价不能为空")
    private Double planBuyPrice;
    /**
     * 计划采购单位
     */
    @Schema(description = "计划采购单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计划采购单位不能为空")
    private String planBuyUnit;
    /**
     * (计划)一般公共预算资金
     */
    @Schema(description = "(计划)一般公共预算资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney1;
    /**
     * (计划)政府性基金预算
     */
    @Schema(description = "(计划)政府性基金预算", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney2;
    /**
     * (计划)其他资金
     */
    @Schema(description = "(计划)其他资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney3;
    /**
     * (计划)非财政性资金
     */
    @Schema(description = "(计划)非财政性资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney4;
    /**
     * (计划)非同级财政拨款
     */
    @Schema(description = "(计划)非同级财政拨款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney5;
    /**
     * (计划)其他单位资金
     */
    @Schema(description = "(计划)其他单位资金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planMoney6;
    /**
     * 计划支付总金额
     */
    @Schema(description = "计划支付总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double planTotalMoney;
    /**
     * 采购分类
     */
    @Schema(description = "采购分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "采购分类不能为空")
    private Integer purCatalogType;
    /**
     * 技术规格，服务及验收内容
     */
    @Schema(description = "技术规格，服务及验收内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spec;
    /**
     * 产品型号
     */
    @Schema(description = "产品型号")
    private String productVersion;
    /**
     * cpu或操作系统信息
     */
    @Schema(description = "cpu或操作系统信息")
    private String cpuAndOsInfo;
    /**
     * 品牌
     */
    @Schema(description = "品牌", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String brand;

}
