package com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 15:28
 */
@Data
public class BpmCompanyPageRespVO {
    private String id;

    /**
     * 公司id
     */
    private Long companyId;
    /**
     * 负责人的用户编号
     */
    private Long leaderUserId;
    /**
     * 提交人名称
     */
    private String submitterName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邀请方式
     * {@link com.yaoan.module.econtract.enums.saas.InviteMethodEnums}
     */
    private Integer inviteMethod;

    /**
     * 邀请方式
     */
    private String inviteMethodStr;

    /**
     * 实名情况
     * {@link com.yaoan.module.econtract.enums.saas.RealNameEnums}
     */
    private Integer realName;
    /**
     * 实名情况
     */
    private String realNameStr;
    /**
     * 用户身份证
     */
    private String userIdCard;

    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 审批结果 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    private String resultName;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 原因
     */
    private String reason;

    /**
     * 终审时间
     */
    private LocalDateTime approveDate;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
