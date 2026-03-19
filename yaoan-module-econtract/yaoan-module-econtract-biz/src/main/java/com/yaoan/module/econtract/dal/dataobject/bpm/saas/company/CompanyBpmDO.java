package com.yaoan.module.econtract.dal.dataobject.bpm.saas.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:38
 */

/**
 * @author Pele
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_company")
public class CompanyBpmDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 8587579774642367L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
     * 公司名称
     */
    private String companyName;

    /**
     * 公司信用代码
     */
    private String companyCreditNo;

    /**
     * 用户身份证
     */
    private String userIdCard;
    /**
     * 终审时间
     */
    private LocalDateTime approveDate;
    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 审批结果 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

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
     * 实名情况
     * {@link com.yaoan.module.econtract.enums.saas.RealNameEnums}
     */
    private Integer realName;
    /**
     * 原因
     */
    private String reason;
}
