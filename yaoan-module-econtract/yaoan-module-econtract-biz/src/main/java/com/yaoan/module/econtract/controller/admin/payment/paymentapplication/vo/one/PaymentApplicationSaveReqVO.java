package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanAmtReqVO;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:42
 */
@Data
public class PaymentApplicationSaveReqVO {


    /**
     * 申请主键
     */
    private String id;

    /**
     * 编码
     */
    private String code;

    /**
     * 标题
     */
    @Size(max = 50, message = "标题最多50字")
    private String title;

    /**
     * 本次付款金额（元）
     */
    private BigDecimal currentPayAmount;

    /**
     * 本期付款金额（大写）
     */
    private String currentPayAmountCapitalize;

    /**
     * 本期付款后付款进度
     */
    private BigDecimal payRate;

    /**
     * 结算方式
     */
    private String settlementMethod;

    /**
     * 付款编号
     */
    private String paymentApplyCode;

    /**
     * 申请人id
     */
    private String applicantId;

    /**
     * 申请人名字
     */
    private String applicantName;

    /**
     * 本期付款后累计已付
     */
    private BigDecimal payedAmount;

    /**
     * 本期付款后剩余应付
     */
    private BigDecimal unpaidAmount;

    /**
     * 付款类型
     * {@link PaymentTypeEnums}
     */
    private Integer paymentType;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 审批状态
     */
    private String flowStatus;

    /**
     * 延期付款日期
     */
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */
    @Size(max = 200, message = "说明最多200字")
    private String reason;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyTime;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 支付计划ids
     */
    private List<PaymentPlanAmtReqVO> buyPlanIds;

    /**
     * 审批结果
     * {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 计划付款金额
     */
    private BigDecimal planAmount;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 是否延期的
     */
    private Boolean isDeferred;

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
     * 附件文件ids
     */
    private List<BusinessFileVO> fileList;
    /**
     * 计划id
     */
    private String planId;

    private Integer sort;

    /**
     * 提交标识
     * 0=不提交
     * 1=提交
     */
    private Integer isSubmit;

    /**
     * 发票集合
     */
    private List<PaymentInvoiceSaveReqVO> invoiceList;

    private String taskId;
}
