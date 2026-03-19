package com.yaoan.module.econtract.dal.dataobject.warningrulemonitor;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警规则与监控项关联关系表（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_rule_monitor_rel")
@KeySequence("ecms_warning_rule_monitor_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningRuleMonitorRelDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 规则id
     */
    private String ruleId;
    /**
     * 监控项id
     */
    private String monitorId;
    /**
     * 检查点id
     */
    private String configId;
}
