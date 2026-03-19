package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * OrganizeStatisticVo
 * @author doujiale
 */
@Data
public class OrganizeStatisticVo implements Serializable {

    private static final long serialVersionUID = 6243469194482543560L;

    private List<StatisticItemVo> details;
    /**
     * 超期合同分数
     */
    private Integer totalExpiredQuantity;
    /**
     * 超期采购单位数
     */
    private Integer totalOrgSize;
}
