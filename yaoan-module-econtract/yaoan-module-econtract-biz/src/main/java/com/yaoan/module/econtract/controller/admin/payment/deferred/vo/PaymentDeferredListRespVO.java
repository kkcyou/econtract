package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 18:55
 */
@Data
public class PaymentDeferredListRespVO {
    /**
     * 申请主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 申请编号
     */
    private String code;

    /**
     * 申请编号
     */
    private String paymentApplyCode;
    /**
     * 申请人id
     */
    private String applyUserId;

    /**
     * 申请人名字
     */
    private String applyUserName;

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
}
