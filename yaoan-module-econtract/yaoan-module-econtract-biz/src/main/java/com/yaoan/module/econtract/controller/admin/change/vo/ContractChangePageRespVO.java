package com.yaoan.module.econtract.controller.admin.change.vo;

import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/25 10:27
 */
@Data
public class ContractChangePageRespVO {
    /**
     * 主键
     */
    private String id;
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

    private String statusName;

    /**
     * 对应的流程编号
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String taskId;


    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 审批时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    /**
     * 变动原因/申请原因
     */
    private String changeReason;

    /**
     * 主合同id（变动合同独有）
     */
    private String mainContractId;
    /**
     * 主合同名称（变动合同独有）
     */
    private String mainContractName;
    /**
     * 主合同编号（变动合同独有）
     */
    private String mainContractCode;
    /**
     * 申请人名称
     */
    private String submitterName;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;

    private String changeTypeName;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     * TO_SEND(0, "TO_SEND", "草稿"),
     * APPROVING(1, "APPROVING", "审批中"),
     * TO_DO(2, "SUCCESS", "审批通过"),
     * REJECTED(5, "TO_SEND", "被退回"),
     */
    private String frontCode;
    /**
     * 申请理由
     */
    private String reason;
    /**
     * 申请状态
     */
    private Integer result;

    private String resultName;

    private String creator;
    private String creatorName;

    /**
     * 是否可以撤回
     * */
    private  Boolean enableRepeal;

    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;

    private LocalDateTime createTime;

    /**
     * 是否可以撤回
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     * */
    private String ifRepeal;
}
