package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.S;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划列表返回参数
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
public class ContractPaymentPlanRespVO {
 /**
  * 支付计划id
  */
 @Schema(description = "支付计划id", requiredMode = Schema.RequiredMode.REQUIRED)
 private  String  id;
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
  * 合同类型
  */
 @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
 private  String  contractType;
    /**
     * 合同类型
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String  contractTypeName;
 /**
  * 我方签约主体
  */
 @Schema(description = "我方签约主体", requiredMode = Schema.RequiredMode.REQUIRED)
 private  String  myContractParty;
    /**
     * 相对方签约主体
     */
    @Schema(description = "相对方签约主体", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> counterparty;
    /**
     * 付款条件
     */
    @Schema(description = "付款条件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String terms;
    /**
     * 付款比例
     */
    @Schema(description = "付款比例", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentRatio;
    /**
     * 付款金额
     */
    @Schema(description = "付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;


    /**
     * 支付状态/付款状态
     */
    @Schema(description = "支付状态/付款状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    /**
     * 支付记录状态
     * 0=未申请  1=已申请  2：草稿箱 3：申请通过
     * {@link PaymentScheduleStatusEnums}
     */
    private Integer  applyStatus;
    /**
     * 支付状态/付款状态名称
     */
    @Schema(description = "支付状态/付款状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String statusName;
    /**
     * 计划支付时间/预计字符时间
     */
    @Schema(description = "计划支付时间/预计支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date paymentTime;

    /**
     * 实际支付时间
     */
    @Schema(description = "实际支付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date actualPayTime;

    /**
     * 支付计划名称
     */
    @Schema(description = "支付计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 创建人
     */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;
    /**
     * 支付期数
     */
    private Integer sort;

    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
}
