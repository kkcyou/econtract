package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 财政部统计项信息
 */
@Data
@XmlRootElement(name = "BillStatisticalInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class BillStatisticalInformation {
    /**
     * (货物类)是否节能产品
     */
    private Boolean efficient;

    /**
     * (货物类)是否节水产品
     */
    private Boolean waterSaving;

    /**
     * (货物类)是否环保产品
     */
    private Boolean environment;

    /**
     * (货物类)额外节能条目
     */
    private String extraEnergySaving;

    /**
     * (车辆)车辆品目类别
     */
    private String carType;

    /**
     * (进口产品)进口产品国别编号
     */
    private String countryCode;

    /**
     * (进口产品)进口产品编号
     */
    private String productCode;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * CPU信息
     */
    private String cpuInfo;

    /**
     * 操作系统信息
     */
    private String operatingSystemsInfo;

    /**
     * 制造商名称
     */
    private String supplierName;

    /**
     * 制造商规模
     */
    private String supplierSize;

    /**
     * 制造商所在区域
     */
    private String zoneCode;

    /**
     * 是否涉及建材产品采购(默认:0)
     */
    private Boolean materialPurchase;

    /**
     * 是否涉及绿色建筑材料采购(默认:0)
     */
    private Boolean greenMaterial;

    /**
     * 建材产品目录Code
     */
    private String materialProductCatalogCode;

    /**
     * 建材产品目录名称
     */
    private String materialProductCatalogName;

    /**
     * (计划)一般公共预算资金
     */
    private Double planMoney1;

    /**
     * (计划)政府性基金预算
     */
    private Double planMoney2;

    /**
     * (计划)其他资金
     */
    private Double planMoney3;

    /**
     * (计划)非财政性资金
     */
    private Double planMoney4;

    /**
     * (计划)非同级财政拨款
     */
    private Double planMoney5;

    /**
     * (计划)其他单位资金
     */
    private Double planMoney6;

    /**
     * (实际)一般公共预算资金
     */
    private Double contractMoney1;

    /**
     * (实际)政府性基金预算
     */
    private Double contractMoney2;

    /**
     * (实际)其他资金
     */
    private Double contractMoney3;

    /**
     * (实际)非财政性资金
     */
    private Double contractMoney4;

    /**
     * (实际)非同级财政拨款
     */
    private Double contractMoney5;

    /**
     * (实际)其他单位资金
     */
    private Double contractMoney6;

    // getters and setters...
}


