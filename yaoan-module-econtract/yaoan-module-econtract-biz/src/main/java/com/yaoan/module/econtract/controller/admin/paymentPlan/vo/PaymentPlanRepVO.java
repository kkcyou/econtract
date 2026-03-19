package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 支付计划列表请求参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
@Schema(description = "支付计划列表请求参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentPlanRepVO  extends PageParam {
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

   @Schema(description = "合同Id", requiredMode = Schema.RequiredMode.REQUIRED)
   private  String  contractId;
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
     * 实际支付开始时间
     */
    @Schema(description = "实际支付开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
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
  * 当前时间
  */
 private Date currentDate;

 /**
  * 计划状态
  */
 private Integer status;

 /**
  * 付款/收款
  */
 private Integer amountType;
 
 private String flag;

 /**
  * 前端标识 identifier
  *  0   全部
  *  1 未开始
  *  2 部分支付
  *  3 支付完成
  *  4 已关闭
  */
 @Schema(description = "查询页面标识" +
         "0   全部\n" +
         "1 未开始\n" +
         "2 部分支付\n" +
         "3 支付完成\n" +
         "4 已关闭", requiredMode = Schema.RequiredMode.REQUIRED)
 @NotNull(message = "查询页面标识不能为空")
 private Integer identifier;
}
