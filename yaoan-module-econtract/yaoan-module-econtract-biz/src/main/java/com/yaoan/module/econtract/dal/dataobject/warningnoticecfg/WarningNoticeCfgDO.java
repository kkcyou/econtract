package com.yaoan.module.econtract.dal.dataobject.warningnoticecfg;

import com.yaoan.module.econtract.enums.warning.WarningNoticeFlowUserTypeEnums;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警通知配置表（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_notice_cfg")
@KeySequence("ecms_warning_notice_cfg_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningNoticeCfgDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 检查点id
     */
    private String configId;
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
     * 0 = 创建人
     * 1 = 当前节点办理人
     * {@link WarningNoticeFlowUserTypeEnums}
     * */
    private Integer flowUserType;
    /**
     * 业务中选择
     */
    private String businessOptionId;
}
