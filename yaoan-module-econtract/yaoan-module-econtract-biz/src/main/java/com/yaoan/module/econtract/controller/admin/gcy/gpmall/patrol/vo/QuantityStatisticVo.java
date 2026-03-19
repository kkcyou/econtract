package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * QuantityStatistic
 * @author doujiale
 */
@Data
public class QuantityStatisticVo implements Serializable {

    private static final long serialVersionUID = 6768249837085889191L;

    /**
     * 月份
     */
    private Integer month;
    /**
     * 超期已签订
     */
    private Integer signed;
    /**
     * 超期未签订
     */
    private Integer unsigned;
}
