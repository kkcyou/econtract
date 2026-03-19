package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;

import java.util.List;

@Data
public class PaymentPageRespVO {
    /**
     * 支付计划返回类
     */
    PageResult<PaymentSchedulePageRespVO> paymentSchedulePageRespVOList;

    /**
     * 支付金额
     */
    PaymentAmount paymentAmount;
}
