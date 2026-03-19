package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:25
 */
@Data
public class PaymentApplicationListReqVO extends PageParam {


    /**
     * 标题
     */
    private String title;

    /**
     * 付款编号
     */
    private String paymentApplyCode;

    /**
     * 申请人id
     */
    private String applicantId;

    /**
     * 申请人名字
     */
    private String applicantName;


    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;


    /**
     * 审批状态
     */
    private Integer result;


    /**
     * 说明 原因
     */
    private String reason;

    /**
     * 申请时间
     */
    private LocalDateTime applyTimeFlag;

    /**
     * 申请时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime0;

    /**
     * 申请时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime1;

    /**
     * 是否延期的
     */
    private Boolean isDeferred;

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


    /**
     * 付款/收款申请状态，0待确认 1已确认
     */
    @Schema(description = "付款/收款申请状态，0待确认 1已确认")
    private Integer status;

    @Schema(description = "通用查询标识")
    private String queryKey;
}
