package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 项目信息
 */
@Data
public class PatrolProjectInfoVO {
    /**
     * 项目所属分类 (A、B、C)--电子交易在合同表，电子卖场，框彩在项目表
     */
    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 项目所属分类名称 (货物、工程、服务)
     */
    @Schema(description = "项目所属分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryName;
//    /**
//     * 采购分类(无过程采购)
//     */
//    @Schema(description = "采购分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purCatalogType;
//    /**
//     * 采购分类名称
//     */
//    @Schema(description = "采购分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purCatalogTypeName;
    /**
     * 采购方式--电子卖场和框彩只有交易方式，无采购方式
     */
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethod;
    /**
     * 采购方式名称
     */
    @Schema(description = "采购方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethodName;

    /**
     * 项目编码--电子卖场，框彩在项目表，电子交易在合同表
     */
    @Schema(description = "项目编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectCode;

    /**
     * 项目名称--电子卖场，框彩在项目表，电子交易在合同表
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectName;


    /**
     * 包编号-电子卖场，框彩无
     */
    @Schema(description = "包编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidCode;
    /**
     * 包名称-电子卖场，框彩无
     */
    @Schema(description = "包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidName;
    /**
     * 中标（成交）时间-电子卖场，框彩取下单时间
     */
    @Schema(description = "中标（成交）时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date bidResultDate;
    /**
     *采购包预算(元)-电子卖场，框彩无
     */
    @Schema(description = "分包预算(元)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double packageMoney;

    /***
     * 中标(成交)金额(元)-电子卖场，框彩取订单总金额
     */
    @Schema(description = "中标(成交)金额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double bidResultMoney;

    /**
     * 采购人名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 代理机构名称(项目采购为必填，电子市场采购为NULL)--电子卖场，框彩无
     */
    @Schema(description = "代理机构名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentName;


    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionName;

}
