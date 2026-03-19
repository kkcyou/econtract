package com.yaoan.module.econtract.dal.dataobject.warningcfg;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警检查配置表(new预警) DO
 *
 * @author admin
 */
@TableName("ecms_warning_cfg")
@KeySequence("ecms_warning_cfg_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningCfgDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 检查点名称
     */
    private String name;
    /**
     * 模块来源
     */
    private String modelId;

    /**
     * 模块编号
     * */
    private String modelCode;
    /**
     * 模块来源名称
     */
    private String modelName;
    /**
     * 启用状态
     */
    private String status;

}
