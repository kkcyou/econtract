package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/26 11:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentApplicationListBpmReqVO extends CommonBpmAutoPageReqVO {
    private static final long serialVersionUID = 6283733682911060229L;
    /**
     * 流程实例
     */
    List<String> instanceIdList;
    /**
     * 付款编号
     */
    private String paymentApplyCode;
    /**
     * 标题
     */
    private String title;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 申请人id
     */
    private String applicantId;
    /**
     * 申请人名字
     */
    private String applicantName;
    /**
     * 申请时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime0;
    /**
     * 申请时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime1;
    /**
     * 是否延期的
     */
    private Boolean isDeferred;

    /**
     * {@link ApprovePageFlagEnums}
     *     ALL(0, "全部"),
     *     DONE(1, "已审批"),
     *     TO_DO(2, "未审批"),
     */
    private Integer flag;

    /**
     * 审批状态
     */
    private Integer result;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     * TO_SEND(0, "TO_SEND", "草稿"),
     * APPROVING(1, "APPROVING", "审批中"),
     * TO_DO(2, "SUCCESS", "审批通过"),
     * REJECTED(5, "TO_SEND", "被退回"),
     */
    private String frontCode;

    private List<String> contractIds;
}
