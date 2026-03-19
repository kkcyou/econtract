package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同明细对应的财政部统计项属性
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractBillStatisticalVo implements Serializable {

    private static final long serialVersionUID = 702065697195025233L;

    private Integer efficient;
    private Integer waterSaving;
    private Integer environment;
    private String extraEnergySaving;
    private String carType;
    private String countryCode;
    private String productCode;
    private String productModel;
    private String cpuInfo;
    private String operatingSystemsInfo;
    private Double planMoney1;
    private Double planMoney2;
    private Double planMoney3;
    private Double planMoney4;
    private Double planMoney5;
    private Double planMoney6;
    private Double contractMoney1;
    private Double contractMoney2;
    private Double contractMoney3;
    private Double contractMoney4;
    private Double contractMoney5;
    private Double contractMoney6;
    private String supplierName;	//制造商名称
    private String supplierSize;	//制造商规模
    private String zoneCode;	//制造商所在区域
}
