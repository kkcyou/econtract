package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 18:24
 */
@Data
public class PaymentApplicationListV2ReqVO extends PageParam {

    private static final long serialVersionUID = 425611118663011905L;

    /**
     * 合同编号或名称
     */
    private String inputStr;

    /**
     * 标题
     */
    private String title;


    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     *     TO_SEND(0, "TO_SEND", "草稿"),
     *     APPROVING(1, "APPROVING", "已审批"),
     *     TO_DO(2, "SUCCESS", "审批通过"),
     *     REJECTED(3, "TO_SEND", "被退回"),
     */
    private String frontCode;

    /**
     * 收款/付款
     */
    private Integer collectType;
}
