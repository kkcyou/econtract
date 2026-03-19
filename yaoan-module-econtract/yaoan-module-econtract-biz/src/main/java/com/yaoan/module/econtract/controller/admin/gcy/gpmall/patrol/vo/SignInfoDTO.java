package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 辅助计算DTO
 * @author doujiale
 */
@Data
public class SignInfoDTO implements Serializable {

    private static final long serialVersionUID = 7066095587243687842L;

    private String id;

    private String orgId;

    private String orgName;

    private Date startDate;

    private Integer startMonth;

    private BigDecimal totalMoney;

    private Date signDate;

    /**
     * 0 未超期 1 超期未签署 2 超期已签署
     */
    private Integer signStatus;
}
