package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/26 11:17
 */
@Data
public class PaymentApplicationListBpmRespVO extends FlowableParam {

    /**
     * 申请主键
     */
    private String id;


    /**
     * 标题
     */
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
     * 结算方式名称
     */
    private String settlementMethodName;
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
     * 付款类型名称
     * {@link PaymentTypeEnums}
     */
    private String paymentTypeName;
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
     * 处理状态
     */
    private String handleStatusStr;

    /**
     * 延期付款日期
     */
    private Date deferredPaymentDate;

    /**
     * 说明 原因
     */

    private String reason;

    /**
     * 申请时间
     */
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
     * 审批结果str
     */
    private String resultStr;

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

    /**
     * 流程任务id
     */
    private String taskId;

    // 状态名称
    private String statusName;
    /**
     * 计划id
     */
    private String planId;

    private Integer contractStatus;

    /**
     * 所属合同是否已冻结
     * */
    private Integer isFreezed;
}
