package com.yaoan.module.econtract.controller.admin.gpx.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/1 11:01
 */
@Data
public class PlanDetailInfoRespVO {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 计划ID
     */
    private String planId;
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
     * 货物计量单位
     */
    private String unit;
    /**
     * 预算（总价）
     */
    private BigDecimal budgetMoney;
    /**
     * 排序序号
     */
    private Integer detailSequence;
    /**
     * 计划明细外部id
     */
    private String outsiteId;

}
