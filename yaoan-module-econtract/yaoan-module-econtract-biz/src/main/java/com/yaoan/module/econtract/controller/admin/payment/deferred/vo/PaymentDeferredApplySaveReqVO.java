package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:34
 */
@Data
public class PaymentDeferredApplySaveReqVO {
    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deferredPaymentDate;


    /**
     * 文件
     */
    private List<BusinessFileVO> fileVOList;

    /**
     * 任务id
     */
    private String taskId;
}
