package com.yaoan.module.econtract.controller.admin.paymentRecord.vo;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 金额相关信息请求参数vo
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
@Schema(description = "金额相关信息请求参数")
@ToString(callSuper = true)
public class PaymentAmountRepVO extends PaymentSchedulePageReqVO {
    /**
     * 请求标识：1：支付计划 2：付款记录
     */
    @Schema(description = "请求标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private  Integer flag;


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

    /**
     * 开始预计支付时间
     */
    @Schema(description = "开始预计支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startPayTime;
    /**
     * 预计支付结束时间
     */
    @Schema(description = "预计支付结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endPayTime;


    /**
     * 支付计划名称/款项名称（搜索框）
     */
    @Schema(description = "支付计划名称/款项名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  paymentPlanName;

    /**
     * 支付状态
     */
    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  paymentStatus;

    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 当前时间
     */
    private Date currentDate;

    /**
     * 收款/付款
     */
    private Integer amountType;
}
