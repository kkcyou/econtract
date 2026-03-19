package com.yaoan.module.econtract.controller.admin.workbench.vo.task;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 17:37
 */
@Data
public class WorkBenchTaskCountRespVO {
    /**
     * 合同审批
     */
    private Long approveCount;
    /**
     * 合同签订
     */
    private Long signCount;
    /**
     * 付款审批
     */
    private Long paymentCount;
    /**
     * 延期计划审批
     */
    private Long deferredCount;
    /**
     * 收款审批
     */
    private Long invoiceCount;
    /**
     * 归档审批
     */
    private Long archiveCount;
    /**
     * 借阅审批
     */
    private Long borrowCount;

}
