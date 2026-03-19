package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import com.yaoan.module.econtract.api.contract.dto.purchasing.ContractBillStatisticalDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 标的信息
 */
@Data
@XmlRootElement(name = "ContractObjectInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractObjectInformation {
    /**
     * 标的编号
     */
    private String contractObjectNumber;

    /**
     * 标的名称
     */
    private String contractObjectName;

    /**
     * 标的说明
     */
    private String contractObjectDesc;

    /**
     * 标的单价
     */
    private Double contractObjectUnitPrice;

    /**
     * 标的数量
     */
    private Double contractObjectQuantity;

    /**
     * 计量单位
     */
    private String unitOfMeasurement;

    /**
     * 标的金额
     */
    private Double contractObjectAmount;

    /**
     * 合同明细序号
     */
    private Integer dOrder;

    /**
     * 采购计划明细识别码
     */
    private String buyPlanBillGuid;

    /**
     * 采购需求识别码
     */
    private String requirementGuid;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 是否进口产品
     */
    private Boolean isImports;

    /**
     * 采购目录代码
     */
    private String purCatalogCode;

    /**
     * 计划采购总价(元)
     */
    private Double planTotalPrice;

    /**
     * 计划采购数量
     */
    private Double planPurchaseNum;

    /**
     * 计划采购单价(元)
     */
    private Double planPrice;

    /**
     * 技术规格及验收内容
     */
    private String spec;

    /**
     * 是否政府购买服务
     */
    private Boolean govService;

    /**
     * 政府购买服务分类
     */
    private String govServiceType;

    /**
     * 政府购买服务目录代码
     */
    private String govServiceCatalogCode;

    /**
     * 财政部统计项属性
     */
    private BillStatisticalInformation billStatisticalInformation;
}

