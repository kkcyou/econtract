package com.yaoan.module.econtract.dal.dataobject.paymentapplication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.enums.payment.PaymentApplicationStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import com.yaoan.module.econtract.enums.payment.SettlementMethodEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description: 付款申请表DO
 * @author: Pele
 * @date: 2023/12/21 14:51
 */
@Data
@TableName("ecms_payment_application")
public class PaymentApplicationDO extends DeptBaseDO {

    private static final long serialVersionUID = -4661725736189610125L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 本次付款金额（元）
     */
    @TableField("current_pay_amount")
    private BigDecimal currentPayAmount;

    /**
     * 本期付款金额（大写）
     */
    @TableField("current_pay_amount_capitalize")
    private String currentPayAmountCapitalize;

    /**
     * 本期付款后付款进度
     */
    @TableField("pay_rate")
    private BigDecimal payRate;

    /**
     * 结算方式
     * 1=转账 2=现金 3=支票
     * {@link SettlementMethodEnums}
     */
    @TableField("settlement_method")
    private String settlementMethod;

    /**
     * 付款编号
     */
    @TableField("payment_apply_code")
    private String paymentApplyCode;

    /**
     * 申请人id
     */
    @TableField("applicant_id")
    private String applicantId;

    /**
     * 申请人名字
     */
    @TableField("applicant_name")
    private String applicantName;

    /**
     * 申请人部门
     */
    @TableField("applicant_dept")
    private String applicantDept;

    /**
     * 本期付款后累计已付
     */
    @TableField("payed_amount")
    private BigDecimal payedAmount;

    /**
     * 本期付款后剩余应付
     */
    @TableField("unpaid_amount")
    private BigDecimal unpaidAmount;
    /**
     * 本期付款后剩余应付
     */
    @TableField("contract_amount")
    private BigDecimal contractAmount;
    /**
     * 付款类型/票款方式
     * {@link PaymentTypeEnums}
     */
    @TableField("payment_type")
    private Integer paymentType;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 审批状态
     */
    @TableField("flow_status")
    private Integer flowStatus;

    /**
     * 延期付款日期
     */
    @TableField("deferred_payment_date")
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */

    private String reason;

    /**
     * 申请时间
     */
    @TableField("apply_time")
    private LocalDateTime applyTime;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批结果
     * {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 是否延期的
     */
    private Boolean isDeferred;
    /**
     * 实付时间
     */
    private Date payDate;

    /**
     * 本次付款后累计实付金额
     */
    private BigDecimal afterPayedAmount;

    /**
     * 付款类型
     * {@link com.yaoan.module.econtract.enums.payment.CollectionTypeEnums}
     */
    @TableField("collection_type")
    private Integer collectionType;

    /**
     * 收款人
     */
    private String payeeName;
    /**
     * 收款方账户
     */
    private String payeeAccount;
    /**
     * 收款人开户行
     */
    private String payeeBankName;

    /**
     * {@link PaymentApplicationStatusEnums}
     * 状态，0待确认 1已确认
     */
    private Integer status;

    private String confirmRemark;

    private LocalDateTime confirmTime;

    /**
     * 实际支付时间
     */
    @Schema(description = "实际支付时间")
    private Date actualPayTime;
}
