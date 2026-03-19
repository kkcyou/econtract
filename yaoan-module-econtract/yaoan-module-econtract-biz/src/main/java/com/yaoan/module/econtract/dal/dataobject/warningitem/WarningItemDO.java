package com.yaoan.module.econtract.dal.dataobject.warningitem;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警事项表（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_item")
@KeySequence("ecms_warning_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningItemDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 检查点id
     */
    private String configId;
    /**
     * 预警事项名称
     */
    private String itemName;
    /**
     * 风险说明
     */
    private String itemRemark;

}
