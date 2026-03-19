package com.yaoan.module.econtract.controller.admin.copyrecipient.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 22:39
 */
@Data
public class CopyRecipientPageRespVO {

    /**
     * 业务名称
     */
    private String name;

    /** 业务编号 */
    private String code;

    /**
     * 申请id
     */
    private String id;

    /**
     * 提交人
     */
    private String submitter;


    /**
     * 审批人
     */
    private String approver;

    /**
     * 审批时间(抄送时间就是审批时间)
     */
    private LocalDateTime createTime;

    private String taskId;

    private String businessId;

    /**
     * 流程状态
     */
    private Integer result;

    private String creator;

    private String processInstanceId;

}
