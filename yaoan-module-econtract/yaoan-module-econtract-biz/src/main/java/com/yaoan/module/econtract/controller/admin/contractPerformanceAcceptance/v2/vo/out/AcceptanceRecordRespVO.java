package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out;

import com.yaoan.module.econtract.enums.AmountTypeEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/25 16:09
 */
@Data
public class AcceptanceRecordRespVO {
    private Long id;

    private Integer sort;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 计划id
     */
    private String planId;
    /**
     * 计划付款金额
     */
    private BigDecimal amount;
    /**
     * 验收时间
     */
    private Date acceptanceDate;

    /**
     * 预计结款时间
     */
    private Date expectedPayDate;

    /**
     * 本次结算金额
     */
    private BigDecimal currentPayMoney;

    /**
     * 本次结算比例
     */
    private BigDecimal currentPayRatio;


    /**
     * 验收备注
     */
    private String remark;

    /**
     * 结算类型
     * {@link AmountTypeEnums}
     */
    private Integer amountType;
    /**
     * 结算类型
     * {@link AmountTypeEnums}
     */
    private String amountTypeName;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private Integer moneyType;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    private String moneyTypeName;
    /**
     * 相对方名称
     */
    private String relativeName;

    private Integer status;

    private String statusName;

    /**
     * 需要验收
     * */
    private Integer needAcceptance;

}
