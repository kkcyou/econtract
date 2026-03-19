// ContactDetalisRespVO.java

package com.yaoan.module.econtract.controller.admin.supervise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 合同详情返回参数 vo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContactDetalisRespVO extends ContactCreateReqVO{
    /**
     *计划名称
     */
    @Schema(description = "计划名称")
    private String buyPlanName;
    /**
     *计划备案/核准书编号
     */
    @Schema(description = "计划备案/核准书编号")
    private String buyPlanCode;
    /**
     *计划金额
     */
    @Schema(description = "计划金额")
    private Double money;
    /**
     * 财政区划代码(参见财政区划定义)-采购人
     */
    private String regionCode;

    /**
     *所在区划-采购人
     */
    @Schema(description = "所在区划")
    private String regionName;
    /**
     *计划所属年度
     */
    @Schema(description = "计划所属年度")
    private Integer year;
    /**
     * 采购实施形式(参见选项字典【Implement】定义)
     */
    private String implement;
    /**
     *实施形式名称
     */
    @Schema(description = "实施形式名称")
    private String implementName;
    /**
     *采购包名称
     */
    @Schema(description = "分包名称")
    private String packageName;
    /**
     *采购包编号
     */
    @Schema(description = "分包编号")
    private Integer packageNumber;
    /**
     *采购包预算(元)
     */
    @Schema(description = "分包预算(元)")
    private Double packageMoney;
    /**
     *采购包最高限价(元)
     */
    @Schema(description = "分包最高限价(元)")
    private Double limitAmount;

//    /**
//     *计划可签约金额/可使用金额
//     */
//    @Schema(description = "计划可签约金额")
//    private Double canUseMoney;
//    /**
//     *已使用金额
//     */
//    @Schema(description = "已使用金额")
//    private Double notUseMoney;
    /**
     * 采购组织形式(参见选项字典【KindEnums】定义)。
     */
    private String kind;
    /**
     * 采购组织形式名称
     */
    private String kindName;
    /**
     * 采购分类(参见选项字典【PurCatalogTypeEnums】定义)
     */
    private String purCatalogType;
    /**
     * 采购分类名称
     */
    private String purCatalogTypeName;
    /**
     * 采购方式编码(参见PurchaseMethodEnums)
     */
    private String purMethod;
    /**
     * 采购方式名称
     */
    private String purMethodName;

    /**
     * 履约保证金最高可支付比例
     */
    private String performanceBondMaxRatio;
    /**
     * 质量保证金最高可支付比例
     */
    private String qualityMarginMaxRatio;


}
