package com.yaoan.module.econtract.controller.admin.ledger.vo.flowable;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 19:39
 */
@Data
public class LedgerFlowableRecordRespVO {

    /**
     * 发起人
     */
    private ApproveStarterRespVO starter;

    /**
     * 审批人list
     */
    private List<ApproverRespVO> approverRespVO;

    /**
     * 审批结果
     */
    private ApproveResultRespVO resultRespVO;

}
