package com.yaoan.module.econtract.controller.admin.warningcfg.vo.get;

import com.yaoan.module.econtract.enums.warning.WarningNoticeFlowUserTypeEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-6-25 18:09
 */
@Data
public class WarningNotice4CfgRespVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 预警规则id
     */
    private String ruleId;
    /**
     * 通知对象选取来源（1.选择用户，2.根据工作流）
     */
    private Integer userType;
    /**
     * 用户角色id
     */
    private Integer userRole;
    /**
     * 用户ids(用逗号分割)
     */
    private String userIds;
    /**
     * 通知方式（1.站内信，2邮件，3短信）
     */
    private Integer noticeWay;
    /**
     * 通知模板
     */
    private String contentTemplate;
    /**
     * 通知次数
     */
    private Integer noticeTimes;

    /**
     * 流程变量对象类型
     * {@link WarningNoticeFlowUserTypeEnums}
     * 0 = 创建人
     * 1 = 当前节点办理人
     */
    private Integer flowUserType;

    /**
     * 业务中选择
     */
    private String businessOptionId;
}
