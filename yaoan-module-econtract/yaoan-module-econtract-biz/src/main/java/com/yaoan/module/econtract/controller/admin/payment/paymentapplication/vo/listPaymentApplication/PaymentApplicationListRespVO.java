package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:25
 */
@Data
public class PaymentApplicationListRespVO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
     * 付款类型
     * {@link PaymentTypeEnums}
     */
    @TableField("payment_type")
    private Integer paymentType;

    /**
     * 付款类型
     * {@link PaymentTypeEnums}
     */
    private String paymentTypeStr;

    /**
     * 结算方式
     */
    private String settlementMethod;

    /**
     * 结算方式
     */
    private String settlementMethodStr;

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
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审批状态
     */
    private Integer result;

    /**
     * 审批状态
     */
    private String resultStr;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;

    /**
     * 收款人-相对方
     */
    private String payeeName;

    /**
     * 合同金额
     */
    private Double contractAmount;

    /**
     * 款项比例
     */
    private BigDecimal payRate;

    /**
     * 款项比例
     */
    private Double paymentRatio;


    private String confirmRemark;

    private LocalDateTime confirmTime;

    /**
     * 实际支付时间
     */
    @Schema(description = "实际支付时间")
    private Date actualPayTime;
    private List<BusinessFileDO> confirmFileList;
}
