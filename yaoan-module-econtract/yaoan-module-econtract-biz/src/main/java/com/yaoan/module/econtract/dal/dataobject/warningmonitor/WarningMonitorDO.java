package com.yaoan.module.econtract.dal.dataobject.warningmonitor;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警监控项配置表（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_monitor")
@KeySequence("ecms_warning_monitor_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningMonitorDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    private String parentId;
    /**
     * 模块code（预留关联关系，暂不维护）
     */
    private String modelId;
    /**
     * 监控项名称
     */
    private String name;
    /**
     * 监控项类型（监控业务数据1，监控流程数据2）
     */
    private Integer type;
    /**
     * 业务表标识
     */
    private String businessCode;
    /**
     * 监控业务表字段
     */
    private String businessField;
    /**
     * 流程key
     */
    private String flowKey;
    /**
     * 流程阶段/节点
     */
    private String flowStage;
    /**
     * 比较类型（0取值，1计差）
     */
    private Integer compareType;
    /**
     * 计差方式（>，<）
     */
    private Integer calculateType;
    /**
     * 内置一个可配置的比较字段
     */
    private String compareStr;

}
