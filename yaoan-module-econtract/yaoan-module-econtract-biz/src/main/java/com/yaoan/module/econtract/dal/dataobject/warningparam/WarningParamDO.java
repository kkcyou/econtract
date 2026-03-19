package com.yaoan.module.econtract.dal.dataobject.warningparam;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警消息模板参数(new预警) DO
 *
 * @author admin
 */
@TableName("ecms_warning_param")
@KeySequence("ecms_warning_param_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningParamDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数配置
     */
    private String paramCfg;
    /**
     * 模块来源
     */
    private String modelId;

    /**
     * 字段名称
     */
    private String fieldStr;

}
