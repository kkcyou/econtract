package com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @description: 计划延期申请
 * @author: Pele
 * @date: 2024/9/30 16:05
 */
@Data
@TableName("ecms_payment_deferred_apply")
public class PaymentDeferredApplyDO extends DeptBaseDO {

    private static final long serialVersionUID = 6354801425827646857L;

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
     * 主键
     */
    private String code;
    /**
     * 计划id
     */
    private String planId;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 申请人id
     */
    private String applyUserId;

    /**
     * 申请人名字
     */
    private String applyUserName;

    /**
     * 说明 原因
     */
    private String reason;

    /**
     * 延期付款日期
     */
    private Date deferredPaymentDate;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批状态
     */
    private Integer flowStatus;

    /**
     * 流程结果
     */
    private Integer result;

    /**
     * 审批完成时间
     */
    private Date approveFinishTime;

    /**
     * 申请时间
     */
    private Date applyTime;
}
