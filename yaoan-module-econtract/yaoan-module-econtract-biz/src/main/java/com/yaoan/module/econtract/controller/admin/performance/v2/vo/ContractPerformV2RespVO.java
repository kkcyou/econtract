package com.yaoan.module.econtract.controller.admin.performance.v2.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/23 15:59
 */
@Data
public class ContractPerformV2RespVO {

    /**
     * 合同id
     */
    private String id;
    /**
     * 合同编码
     */
    private String code;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 签署完成时间
     */
    private Date signDate;
    /**
     * 合同类型id
     */
    private String contractType;
    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 合同状态编码
     */
    private Integer status;
    /**
     * 合同状态值
     */
    private String statusName;

    /**
     * 履约任务完成情况
     */
    private String finishInfo;

    /**
     * 履约最终截止时间，展示合同下所有履约任务中履约时间最靠后的时间
     */
    private Date perfTime;

    /**
     * 备注天数
     */
    private Integer remarkDays;

    /**
     * 暂停时间
     */
    private Date pauseDate;
    /**
     * 支付期数
     */
    private Integer sort;

    /**
     * 付款金额
     */
    private BigDecimal amount;
}
