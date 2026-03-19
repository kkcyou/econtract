package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one;

import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out.AcceptanceRecordRespVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:33
 */
@Data
public class PaymentApplicationRespVO {

    /**
     * 基本信息
     */
    private PaymentApplicationBaseInfoRespVO paymentApplicationBaseInfoRespVO;

    /**
     * 本次支付计划
     */
    private List<PaymentPlanRespVO> paymentPlanRespVOList;

    /**
     * 合同信息
     */
    private ContractInfoRespVO contractInfoRespVO;

    /**
     * 收款方信息
     */
    private PayeeInfoRespVO payeeInfoRespVO;

    /**
     * 附件文件id
     */
    private Long attachmentId;

    /**
     * 处理意见
     * （审批人的详情页面展示）
     */
    private String approveAdvice;

    /**
     * 实付时间
     */
    private Date payDate;

    private List<BusinessFileVO> fileRespVOList;

    @Schema(description = "发票信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PaymentInvoiceRespVO> invoiceRespVOS;

    /**
     * 验收记录
     */
    private List<AcceptanceRecordRespVO> acceptanceRespVOList;


}
