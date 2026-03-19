package com.yaoan.module.econtract.dal.dataobject.warningmodel;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警模块来源（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_model")
@KeySequence("ecms_warning_model_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningModelDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级节点id
     */
    private String parentId;
    /**
     * 对应工厂名称
     */
    private String factoryName;

}
