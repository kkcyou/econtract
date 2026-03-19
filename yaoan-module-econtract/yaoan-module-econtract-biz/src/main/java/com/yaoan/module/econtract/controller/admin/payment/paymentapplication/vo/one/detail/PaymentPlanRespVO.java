package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail;

import com.yaoan.module.econtract.enums.AmountTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 20:17
 */
@Data
public class PaymentPlanRespVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 支付期数
     */
    private Integer sort;


    /**
     * 付款条件
     */
    private String terms;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 付款比例
     */
    private Double paymentRatio;

    /**
     * 未发起支付金额
     */
    private BigDecimal unPayAmount;

    private BigDecimal currentPayAmount;

    /**
     * 履约计划类型
     * {@link AmountTypeEnums}
     */
    @Schema(description = "履约计划类型")
    private Integer amountType;
    /**
     * 款项类型 首付款1 进度款2 尾款3
     */
    @Schema(description = "款项类型 首付款1 进度款2 尾款3")
    private Integer moneyType;

    /**
     * 收款人-主体id
     */
    private String payee;
    private String payeeName;

    private Integer needAcceptance;

    private Integer isAcceptance;

    /**
     * （1）需验收且验收通过=可选；
     * （2）需要验收且未验收通过=请先完成验收；
     * （3）已验收且已发起付款后的=不可选；
     * （4）不需验收计划 = 本次金额不大于剩余结算金额；
     * */
    private Integer acceptanceFlag;
    private Integer status;

    /**
     * 验收的金额
     * */
    private BigDecimal acceptanceAmount;
}
