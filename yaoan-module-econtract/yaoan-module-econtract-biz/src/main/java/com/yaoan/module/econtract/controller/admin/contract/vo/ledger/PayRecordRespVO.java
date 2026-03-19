package com.yaoan.module.econtract.controller.admin.contract.vo.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.payment.SettlementMethodEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-23 15:00
 */
@Data
public class PayRecordRespVO {

    private String id;

    private String submitterName;

//    /**
//     * 期数
//     */
//    private Integer sort;

    /**
     * 款项比例
     */
    private BigDecimal paymentRatio;



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
//    /**
//     * 款项类型 首付款1 进度款2 尾款3
//     */
//    private Integer moneyType;
//    /**
//     * 款项类型 首付款1 进度款2 尾款3
//     */
//    private String moneyTypeName;
    /**
     * 相对方名称
     */
    private String relativeName;

    /**
     * 纳税人识别号
     */
    private String buyerNumber;
    /**
     * 创建时间
     */
    private LocalDateTime updateTime;

//    /**
//     * 已结算金额
//     */
//    private BigDecimal settledAmount;
//
//    /**
//     * 已结算比例
//     */
//    private BigDecimal settledRatio;

    /**
     * 计划支付时间
     */
    private Date paymentTime;
    /**
     * 需要验收
     * 1=需要
     * 0=不需要
     * */
    private Integer needAcceptance;
    /**
     * 支付金额
     * */
    private BigDecimal applyAmount;
//    /**
//     * 已验收金额
//     * */
//    private BigDecimal acceptanceMoney;
//    /**
//     * 已付款金额
//     */
//    @Schema(description = "已付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
//    private BigDecimal paidAmount;
    /**
     * 申请时间
     */
    @Schema(description = "申请时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date applyTime;
    /**
     * 结算方式
     * 1=转账 2=现金 3=支票
     * {@link SettlementMethodEnums}
     */
    @TableField("settlement_method")
    private String settlementMethod;

    /**
     * 结算方式
     * 1=转账 2=现金 3=支票
     * {@link SettlementMethodEnums}
     */
    @TableField("settlement_method")
    private String settlementMethodStr;

    private String nodeName;
    /**
     * 收款方银行账号
     */
    private String bankAccount;

    /**
     * 收款方开户行
     */
    private String bankName;

    /**
     * ======================= 前端需要 =========================
     * */



    /**
     * 支付计划ids
     */
    private List<String> buyPlanIds;

    private String contractId;

    private String processInstanceId;


    private String statusName;
//    private String taskId;

    private Integer status;



}
