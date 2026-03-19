package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:43
 */
@Data
public class PaymentApplicationUpdateReqVO {


    /**
     * 申请主键
     */
    @TableField("title")
    private String id;

    /**
     * 标题
     */
    @Size(max = 50, message = "标题最多50字")
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
     * 付款类型
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
    private String flowStatus;

    /**
     * 延期付款日期
     */
    @TableField("deferred_payment_date")
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */
    @Size(max = 200, message = "说明最多200字")
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
     * 支付计划ids
     */
    private List<String> buyPlanIds;

    /**
     * 审批结果
     * {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 计划付款金额
     */
    @TableField("payed_amount")
    private BigDecimal planAmount;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 是否延期的
     */
    private Boolean isDeferred;
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
     * 附件文件ids
     */
    private List<BusinessFileVO> fileList;
}
