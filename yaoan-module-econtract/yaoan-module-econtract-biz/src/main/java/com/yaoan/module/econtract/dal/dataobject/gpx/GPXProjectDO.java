package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 17:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_project")
public class GPXProjectDO extends BaseDO {
    private static final long serialVersionUID = 7572264253574538856L;
    /**
     * 项目id
     */
    @TableId(value = "project_guid")
    private String projectGuid;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 开标地点
     */
    private String bidAddress;
    /**Da   * 开标时间
     */
    private Date bidOpenTime;
    /**
     * 采购方式
     */
    private String purchaseMethodCode;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;
    /**
     * 项目预算
     */
    private Double budget;

    /**
     * 代理id
     */
    private String agencyId;
    /**
     * 代理机构名称
     */
    private String agencyName;
    /**
     * 项目类型（货物、服务、工程）
     */
    private String projectType;
    /**
     * 项目类型名称
     */
    private String projectTypeName;
    /**
     * 项目负责人
     */
    private String managerId;
    /**
     * 项目负责人名称
     */
    private String managerName;
    /**
     * 是否联合采购(1:是,0:否)
     */
    private String unionPurchaseStatus;
    /**
     * 招投标方式编码：online offline
     */
    private String projectBiddingType;
    /**
     * 招投标方式名称：线上招投标 线下招投标
     */
    private String projectBiddingName;
    /**
     * 数据接口来源（默认为null,从主控导入的为saas）
     */
    private String importSource;
    /**
     * 供应商征集方式 0公告方式，1邀请方式
     */
    private String supplierSolicitationMethod;
    /**
     * 招标项目方式名称：common:一般项目采购、batch:批量集中采购、union:联合采购、other:其他
     */
    private String biddingMethodCode;
    /**
     * 招标项目方式名称：一般项目采购、批量集中采购、联合采购、其他
     */
    private String biddingMethodName;
    /**
     * 区域编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 采购实施形式类型：101一般项目采购，201框架协议采购，301批量采购归集
     */
    private String implement;
    /**
     * 采购实施形式类型名称：101一般项目采购，201框架协议采购，301批量采购归集
     */
    private String implementName;
    /**
     * 数据来源 saas/normal saas版本/项目版
     */
    private String dataSource;
    /**
     * 是否PPP项目(1:是,0:否)
     */
    private Integer isPpp;


}
