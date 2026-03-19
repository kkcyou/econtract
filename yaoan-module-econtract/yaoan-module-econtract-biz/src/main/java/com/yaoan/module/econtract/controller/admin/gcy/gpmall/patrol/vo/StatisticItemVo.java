package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * StatisticItemVo
 * @author doujiale
 */
@Data
public class StatisticItemVo implements Serializable {

    private static final long serialVersionUID = 5639384628003169776L;
    /**
     * 超期数量
     */
    private Integer expiredQuantity;
    /**
     * 采购单位名称
     */
    private String orgName;
}
