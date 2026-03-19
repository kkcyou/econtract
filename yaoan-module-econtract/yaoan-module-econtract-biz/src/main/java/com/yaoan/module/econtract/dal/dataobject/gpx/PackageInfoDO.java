package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 17:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_package")
public class PackageInfoDO extends BaseDO {
    private static final long serialVersionUID = 1270952462977811951L;
    /**
     * 包id
     */
    @TableId(value = "package_guid")
    private String packageGuid;
    /**
     * 包号
     */
    private Integer packageNumber;
    /**
     * 包名称
     */
    private String packageName;
    /**
     * 包金额
     */
    private Double amount;
    /**
     * 中标价格
     */
    private Double winBidAmount;
    /**
     * 中标时间
     */
    private Date winBidTime;
    /**
     * 开标时间
     */
    private Date bidOpenTime;
    /**
     * 评审结束时间
     */
    private Date reviewEndTime;
    /**
     * 开标地点
     */
    private String bidOpenPlace;
    /**
     * 是否流标,0为不流标，1为流标，默认0
     */
    private Integer isLost;
    /**
     * 区划
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 价款形式
     */
    private String priceMode;
    /**
     * 价款形式名称
     */
    private String priceModeName;
    /**
     * 定价方式编码（合同），常量值
     */
    private String contractPriceMethodCode;
    /**
     * 定价方式名称（合同）
     */
    private String contractPriceMethodName;
    /**
     * 是否适宜中小企业采购0否1是
     */
    private String suitableStatus;
    /**
     * 预留形式编码
     */
    private String executiveModeCode;
    /**
     * 预留形式名称
     */
    private String executiveModeName;
    /**
     * 面向的企业规模编码
     */
    private String supplierReserveCode;
    /**
     * 面向的企业规模名称
     */
    private String supplierReserveName;
    /**
     * 预留比例
     */
    private BigDecimal reserveRatio;


    /**
     * 招标项目方式名称：common:一般项目采购、batch:批量集中采购、union:联合采购、other:其他
     */
    private String biddingMethodCode;
    /**
     * 招标项目方式名称：一般项目采购、批量集中采购、联合采购、其他
     */
    private String biddingMethodName;

    /**
     * 采购单位ids
     */
    private String purchaserOrgIds;
    /**
     * 中标的供应商信息
     */
    private String supplierIds;



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
     * 是否是多供应商
     * 1=单供应商，2=多供应商
     */
    private Integer supplierType;
    /**
     * 是否隐藏（一般项目采购多供应商场景，新加）
     * */
    private Integer hidden;

}
