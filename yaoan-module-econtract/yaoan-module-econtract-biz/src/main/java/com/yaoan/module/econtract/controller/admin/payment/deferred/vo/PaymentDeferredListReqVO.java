package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 18:44
 */
@Data
public class PaymentDeferredListReqVO extends PageParam {

    private static final long serialVersionUID = 8708908123183499768L;
    /**
     * 标题
     */
    private String title;

    /**
     * 合同编号或名称
     */
    private String inputStr;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     *     TO_SEND(0, "TO_SEND", "草稿"),
     *     APPROVING(1, "APPROVING", "已审批"),
     *     TO_DO(2, "SUCCESS", "审批通过"),
     *     REJECTED(3, "TO_SEND", "被退回"),
     */
    private String frontCode;
}
