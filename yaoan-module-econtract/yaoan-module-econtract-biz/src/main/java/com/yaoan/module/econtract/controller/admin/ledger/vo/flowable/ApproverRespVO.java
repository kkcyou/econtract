package com.yaoan.module.econtract.controller.admin.ledger.vo.flowable;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 19:49
 */
@Data
public class ApproverRespVO {

    /**
     * 审批人名称
     */
    private String approveName;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;


}
