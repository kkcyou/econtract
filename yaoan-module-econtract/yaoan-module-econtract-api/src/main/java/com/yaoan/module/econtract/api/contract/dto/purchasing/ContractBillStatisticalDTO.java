package com.yaoan.module.econtract.api.contract.dto.purchasing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 合同明细对应的财政部统计项属性 请求参数 vo
 */
@Data
@XmlRootElement(name = "ContractBillStatisticalDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractBillStatisticalDTO {
    /**
     * (货物类)是否节能产品(1:是,0:否)
     */
    @Schema(description = "是否节能产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer efficient;
    /**
     * (货物类)是否节水产品(1:是,0:否)
     */
    @Schema(description = "是否节水产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer waterSaving;
    /**
     * (货物类)是否环保产品(1:是,0:否)
     */
    @Schema(description = "是否环保产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer environment;
    /**
     * (货物类)额外节能条目
     */
    @Schema(description = "额外节能条目", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String extraEnergySaving;
    /**
     * (车辆)车辆品目类别
     */
    @Schema(description = "车辆品目类别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String carType;
    /**
     * (进口产品)进口产品国别编号
     */
    @Schema(description = "进口产品国别编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryCode;
    /**
     * (进口产品)进口产品编号
     */
    @Schema(description = "进口产品编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String productCode;
    /**
     * (计划)一般公共预算资金
     */
    @Schema(description = "一般公共预算资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double planMoney1;

    /**
     * (计划)政府性基金预算
     */
    @Schema(description = "政府性基金预算", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double planMoney2;
    /**
     * (计划)其他资金
     */
    @Schema(description = "其他资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double planMoney3;
    /**
     * (计划)非财政性资金
     */
    @Schema(description = "非财政性资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double planMoney4;
    /**
     * (计划)非同级财政拨款
     */
    @Schema(description = "非同级财政拨款", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double planMoney5;
    /**
     * (实际)一般公共预算资金
     */
    @Schema(description = "一般公共预算资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney1;
    /**
     * (实际)政府性基金预算
     */
    @Schema(description = "非同级财政拨款", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney2;
    /**
     * (实际)其他资金
     */
    @Schema(description = "其他资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney3;
    /**
     * (实际)非财政性资金
     */
    @Schema(description = "非财政性资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney4;
    /**
     * (实际)非同级财政拨款
     */
    @Schema(description = "非同级财政拨款", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney5;
    /**
     * (实际)其他单位资金
     */
    @Schema(description = "其他单位资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double contractMoney6;

}
