package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:55
 */
@Data
public class PaymentDeferredApplyUpdateReqVO {
    private String id;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 合同id
     */
    private String contractId;

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
     * 文件
     */
    private List<BusinessFileVO> fileVOList;
}
