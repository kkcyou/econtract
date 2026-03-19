package com.yaoan.module.econtract.controller.admin.paymentRecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划列表请求参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
public class PaymentRecordRespVO {
 /**
  * 付款id
  */
 @Schema(description = "付款id", requiredMode = Schema.RequiredMode.REQUIRED)
 private  String  id;
    /**
     * 付款编码
     */
    @Schema(description = "付款编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentApplyCode;

    /**
     * 付款标题、付款名称
     */
    @Schema(description = "付款标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  contractId;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
   private  String  contractName;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  contractCode;
    /**
     * 申请人
     */
    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantName;

 /**
  * 收款方（相对方）
  */
    @Schema(description = "相对方签约主体", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> counterparty;

    /**
     * 付款金额
     */
    @Schema(description = "付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal currentPayAmount;


    /**
     * 实际支付时间
     */
    @Schema(description = "实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date actualPayTime;




}
