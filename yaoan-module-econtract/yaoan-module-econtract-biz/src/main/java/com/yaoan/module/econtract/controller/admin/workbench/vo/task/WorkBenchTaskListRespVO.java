package com.yaoan.module.econtract.controller.admin.workbench.vo.task;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 17:28
 */
@Data
public class WorkBenchTaskListRespVO {
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同编号
     */
    private String code;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型名称
     */
    private String contractTypeName;
    /**
     * 合同金额
     */
    private Double amount;
    /**
     * 起草时间
     */
    private LocalDateTime createTime;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 收/付款id
     */
    private String payeeId;

    /**
     * 标题
     */
    private String title;
    /**
     * 本次付款金额（元）
     */
    private BigDecimal currentPayAmount;

    /**
     * 支付计划ids
     */
    private List<String> buyPlanIds;

    /**
     * 用印id
     * */
    private String bpmContractSignetId;


}
