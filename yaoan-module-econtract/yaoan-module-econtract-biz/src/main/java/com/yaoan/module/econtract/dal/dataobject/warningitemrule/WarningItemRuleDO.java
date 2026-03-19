package com.yaoan.module.econtract.dal.dataobject.warningitemrule;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预警规则（new预警） DO
 *
 * @author admin
 */
@TableName("ecms_warning_item_rule")
@KeySequence("ecms_warning_item_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningItemRuleDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 检查点id
     * */
    private String configId;
    /**
     * 规则名称
     * */
    private String name;
    /**
     * 预警事项id
     */
    private String warningItemId;
    /**
     * 预警事项名称
     */
    private String warningItemName;
    /**
     * 监控项id
     */
    private String monitorItemId;
    /**
     * 监控项名称
     */
    private String monitorItemName;
    /**
     * 比较类型（大于小于等于范围不等于立即执行）
     */
    private Integer compareType;
    /**
     * 比较项1/阈值（整型）
     */
    private Integer compareItemStart;
    /**
     * 比较项2/阈值  (整型)
     */
    private Integer compareItemEnd;
    /**
     * 比较项1（浮点型，为金额和百分比预留）
     */
    private BigDecimal compareDecItemStart;
    /**
     * 比较项2（浮点型，为金额和百分比预留）
     */
    private BigDecimal compareDecItemEnd;
    /**
     * 比较项1（日期类型，预留）
     */
    private LocalDateTime compareDateItemStart;
    /**
     * 比较项2（日期类型，预留）
     */
    private LocalDateTime compareDateItemEnd;
    /**
     * 阈值单位（自然日，工作日，金额，数量，百分比）
     */
    private Integer compareDataType;
    /**
     *  通知频率类型(0通知一次，1每天通知，2自定义通知间隔天数)
     */
    private Integer noticeTimesType;
    /**
     * 通知间隔天数
     */
    private Integer noticeTimesDay;

    /**
     * 是否需要通知
     * */
    private Integer isSendMsg;

    private Long tenantId;
}
