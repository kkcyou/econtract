package com.yaoan.module.econtract.controller.admin.change.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/25 10:27
 */
@Data
public class ContractChangePageReqVO extends PageParam {
    /**
     * 合同编码
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;


    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    private Integer status;

    /**
     * 合同金额
     */
    private Double amount;


    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime0;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime1;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     * TO_SEND(0, "TO_SEND", "草稿"),
     * APPROVING(1, "APPROVING", "审批中"),
     * TO_DO(2, "SUCCESS", "审批通过"),
     * REJECTED(5, "TO_SEND", "被退回"),
     */
    private String frontCode;

    private List<Integer> frontCodes;


    /**
     * 变动类型（1=变更，2=补充，3=解除,4=关闭，5=取消，6=作废）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private List<Integer> changeTypes;


    /**
     * 判断状态变更列表查询标识
     * 变更类型 1 关闭 ; 2 取消和作废；
     */
    private Integer flag;
}
