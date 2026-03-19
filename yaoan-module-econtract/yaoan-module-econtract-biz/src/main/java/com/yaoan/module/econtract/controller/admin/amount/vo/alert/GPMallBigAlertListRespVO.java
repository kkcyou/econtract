package com.yaoan.module.econtract.controller.admin.amount.vo.alert;

import lombok.Data;

import java.util.List;

@Data
public class GPMallBigAlertListRespVO {
    /**
     * 待确认
     */
    List<GPMallAlertListRespVO> toBeSent;

    /**
     * 待签署
     */
    List<GPMallAlertListRespVO> toBeSigned;

    /**
     * 内部审批
     */
    List<GPMallAlertListRespVO> innerApprove;
    /**
     * 待起草
     */
//    List<GPMallAlertListRespVO> toBeDrafted;
}