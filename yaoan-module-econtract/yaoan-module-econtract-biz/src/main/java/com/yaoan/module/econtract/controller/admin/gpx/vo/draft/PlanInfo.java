package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 18:19
 */
@Data
public class PlanInfo {
    /**
     * 计划id
     */
    private String id;
    /**
     * 计划code
     */
    private String planCode;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 采购单位编号
     */
    private String purchaseUnitId;
    /**
     * 采购单位名称
     */
    private String purchaseUnitName;
    /**
     * 联系人
     */
    private String purchaseLinkName;
    /**
     * 采购单位联系电话
     */
    private String purchaseLinkTel;
    /**
     * 代理机构名称
     */
    private String agencyName;
    /**
     * 代理机构ID
     */
    private String agencyId;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;
    /**
     * 采购方式编码
     */
    private String purchaseMethodCode;
    /**
     * 组织形式名称
     */
    private String orgMethodName;
    /**
     * 组织形式编码
     */
    private String orgMethodCode;
    /**
     * 计划类别编码
     */
    private String planType;
    /**
     * 计划类别名称
     */
    private String planTypeName;
    /**
     * 项目类型编码
     */
    private String planCategory;
    /**
     * 项目类型名称
     */
    private String planCategoryName;
    /**
     * 预算总价
     */
    private BigDecimal planBudget;
    /**
     * 预算年度
     */
    private String budgetYear;
    /**
     * 服务类型编码
     */
    private String sericeTypeCode;
    /**
     * 服务类型
     */
    private String sericeType;
    /**
     * 采购用途
     */
    private String purchasePurpose;
    /**
     * 采购用途编码
     */
    private String purchasePurposeCode;
    /**
     * 状态编码
     */
    private String statusCode;
    /**
     * 状态名称
     */
    private String statusName;
    /**
     * 数据来源
     */
    private String source;
    /**
     * 区域编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 计划负责人id
     */
    private String planLeaderId;
    /**
     * 计划负责人名称
     */
    private String planLeaderName;
    /**
     * 是否PPP项目(0/1 否/是)
     */
    private String isPpp;
    /**
     * 计划来源编码
     */
    private String sourceCode;
    /**
     * 采购实施形式类型：101一般项目采购，201框架协议采购，301批量采购归集
     */
    private String implement;
    /**
     * 采购实施形式类型名称：101一般项目采购，201框架协议采购，301批量采购归集
     */
    private String implementName;
    /**
     * 是否联合采购
     */
    private Integer unionPurchaseStatus;
    /**
     * 外部计划ID
     */
    private String outSitePlanId;
    /**
     * 最高限价
     */
    private BigDecimal limitMoney;
    /**
     * 计划明细信息
     */
    private List<PlanDetailInfo> planDetailInfo;

    private BigDecimal planAmount;
    private BigDecimal toSignAmount;
}
