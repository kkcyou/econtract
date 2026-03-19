package com.yaoan.module.econtract.controller.admin.paymentRecord.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 支付计划列表请求参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
@Schema(description = "支付记录列表请求参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentRecordRepVO extends PageParam {

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
     * 付款编码
     */
    @Schema(description = "付款编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  paymentCode;
 /**
  * 付款标题
  */
 @Schema(description = "付款标题", requiredMode = Schema.RequiredMode.REQUIRED)
 private String title;

    /**
     * 申请人
     */
    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantName;

    /**
     * 实际支付开始时间
     */
    @Schema(description = "开始实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startActualPayTime;
    /**
     * 实际支付结束时间
     */
    @Schema(description = "实际支付结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endActualPayTime;

    @Schema(description = "收款/付款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date amountType;

}
