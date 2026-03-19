package com.yaoan.module.econtract.controller.admin.gpx.vo.autofill;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/2 22:47
 */
@Data
public class PackageDetailRespVO {

    /**
     * 品目编号
     */
    private String catalogueCode;
    /**
     * 品目名称
     */
    private String catalogueName;
    /**
     * 明细名称
     */
    private String deatilName;
    /**
     * 品牌
     */
    private String brand;

    /**
     * 规格型号
     */
    private String model;
    /**
     * 数量
     */
    private BigDecimal detailNumber;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 货物计量单位
     */
    private String unit;

    /**
     * 服务年限
     */
    private String serviceLife;

    /**
     * 所属行业编码
     */
    private String industryCode;
    /**
     * 所属行业名称
     */
    private String industryName;


}
