package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: doujl
 * @date: 2024/5/22 10:02
 */
@Data
public class GPXContractProjectVO implements Serializable {

    private static final long serialVersionUID = 978441925640604131L;

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
     * 供应商id
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 联系人地址
     */
    private String personAddr;
    /**
     * 联系人电话
     */
    private String personMobile;
    /**
     * 联系人
     */
    private String personName;
    /**
     * 总价模式
     */
    private String priceMode;
    /**
     * 模式名称
     */
    private String priceModeName;
    /**
     * 区划
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;

}
