package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划返回类
 */
@Data
public class PayPerformanceDetailRespVO {
    /**
     * 支付计划名称
     */
    private String name;

    /**
     * 合同id
     */
    private String contractId;
    private String contractCode;
    private String contractName;
    

    /**
     * 支付期数
     */
    private Integer sort;

    /**
     * 收款人-主体id
     */
    private String payee;

    /**
     * 付款条件
     */
    private String terms;

    /**
     * 付款时间
     */
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date paymentTime;

    /**
     * 付款比例
     */
    private Double paymentRatio;

    /**
     * 付款金额
     */
    private BigDecimal amount;
    private String status;
    /**
     * 履约状态
     */
    private String statusName;

    /**
     * 履约计划类型
     *
     */
    private Integer amountType;

    private String amountTypeName;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;
    private String moneyTypeName;
    /**
     * 是否提醒 不提醒0 提醒1
     */
    private Integer isRemind;
    /**
     * 提醒方式 系统消息:message
     */
    private String remindType;
    /**
     * 提醒时间
     */
    private Date paymentRemindTime;
    
    private List<BusinessFileDO> confirmFileList;
    
    private String confirmRemark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;


}
