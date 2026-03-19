package com.yaoan.module.econtract.controller.admin.warning.vo;
import lombok.Data;

import java.util.Date;

@Data
public class WarningInfoVO {
    /**
     * 中标成交时间
     */
    private Date winBidTime;

    /**
     * 超期天数
     */
    private Long overDays;

    /**
     * 签订截止日期
     */
    private Date contractSignEndTime;

    /**
     * 合同是否签订 0：未签订  1：已签订
     */
    private Integer isSign;

    /**
     * 实际签订日期
     */
    private Date contractSignTime;

    /**
     * 预警级别
     */
    private String warningLevel;




}
