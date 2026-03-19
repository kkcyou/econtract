package com.yaoan.module.econtract.dal.dataobject.warningruleconfig;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 预警规则配置 DO
 *
 * @author lls
 */
@TableName("ecms_warning_rule_config")
@KeySequence("ecms_warning_rule_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningRuleConfigDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 检查点id
     */
    private String configId;
    /**
     * 规则编码
     */
    private String warningRuleCode;
    /**
     * 规则名称
     */
    private String warningRuleName;
    /**
     * 规则类型 事前提醒1 事中提醒2
     */
    private Integer warningRuleType;
    /**
     * 规则描述
     */
    private String warningRuleRemark;
    /**
     * 所属业务
     */
    private String businessTypeId;
    /**
     * 触发字段
     */
    private String warningRuleField;
    /**
     * 是否启用配置，0关闭，1启用
     */
    private String enable;
    /**
     * 逾期是否业务阻断，1阻断，0不阻断
     */
    private Integer isBlocked;
    /**
     * 所属公司
     */
    private Long companyId;

}