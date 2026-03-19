package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 17:50
 */
@Data
public class PackageInfo {
    /**
     * 包id
     */
    private String id;
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
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date winBidTime;
    /**
     * 评审结束时间
     */
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date reviewEndTime;
    /**
     * 开标时间
     */
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date bidOpenTime;
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
     * 明细数据
     */
    private List<PackageDetailInfo> packageDetailInfoList;
    /**
     * 中标的供应商信息
     */
    private List<SupplierInfo> supplierList;

    /**
     * 所属的项目guid
     */
    private String projectGuid;
}
