package com.yaoan.module.econtract.controller.admin.gpx.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/22 10:02
 */
@Data
public class GPXListRespVO {


    /**
     * 所属的项目guid
     */
    private String projectGuid;
    /**
     * 项目类型（货物、服务、工程）
     */
    private String projectType;
    /**
     * 项目类型名称
     */
    private String projectTypeName;

    /**
     * 采购方式
     */
    private String purchaseMethodCode;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 项目负责人
     */
    private String managerId;
    /**
     * 项目负责人名称
     */
    private String managerName;
    /**
     * 是否PPP项目(1:是,0:否)
     */
    private Integer isPpp;

    /**
     * 分包id
     */
    private String packageGuid;
    /**
     * 分包数
     */
    private String packageNumber;
    /**
     * 分包名称
     */
    private String packageName;
    /**
     * 预算 （包明细）
     */
    private BigDecimal amount;
    /**
     * 中标金额
     */
    private BigDecimal winBidAmount;
    /**
     * 中标成交日期
     */
    private Date winBidTime;
    private String winBidTimeBack;

    /**
     * 代理机构id
     */
    private String agencyId;
    /**
     * 代理机构名称
     */
    private String agencyName;
    /**
     * 采购人单位id
     */
    private String purchaserId;
    /**
     * 采购人单位名称
     */
    private String purchaserOrgName;
    /**
     * 采购负责人电话
     */
    private String purchaserLinkTel;
    /**
     * 采购负责人
     */
    private String purchaserLinkName;

    /**
     * 总价模式
     */
    private String priceMode;
    /**
     * 模式名称
     */
    private String priceModeName;


    private List<PlanRespVO> plans;


    private List<SupplierRespVO> supplierList;
    /**
     * 区划
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 可签约金额
     */
    private BigDecimal toSignAmount;

    /**
     * 已签约金额
     */
    private BigDecimal signedAmount;

    /**
     * 是否信用评价
     */
    private Boolean haveValuated;

    /**
     * 招标项目方式名称：common:一般项目采购、batch:批量集中采购、union:联合采购、other:其他
     */
    private String biddingMethodCode;

    /**
     * 是否起草
     */
    private Boolean isDraft;
}
