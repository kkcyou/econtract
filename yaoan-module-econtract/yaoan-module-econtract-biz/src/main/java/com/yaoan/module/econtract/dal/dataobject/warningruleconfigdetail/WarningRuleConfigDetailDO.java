package com.yaoan.module.econtract.dal.dataobject.warningruleconfigdetail;


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
 * 预警规则明细 DO
 *
 * @author lls
 */
@TableName("ecms_warning_rule_config_detail")
@KeySequence("ecms_warning_rule_config_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningRuleConfigDetailDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 规则id
     */
    private String warningRuleId;
    /**
     * 是否启用配置，0关闭，1启用
     */
    private String enable;
    /**
     * 提醒类型
     */
    private String warningType;

    /**
     * 提醒标准
     */
    private Integer warningDays;
    /**
     * 提醒类型表达式
     */
    private String warningTypeExpression;
    /**
     * 触发条件
     */
    private String warningCondition;
    /**
     * 风险等级 低风险1 中风险2 高风险3
     */
    private Integer warningLevel;
    /**
     * 推送方式(多选),站内信message/短信sms/邮件email
     */
    private String pushType;
    /**
     * 推送对象(多选),经办人creator/合同管理员admin
     */
    private String pushTarget;
    /**
     * 消息模板
     */
    private Long pushTemplateId;
    /**
     * 短信模板渠道 发送短信的平台
     */
    private Integer pushChannel;
    /**
     * 所属公司
     */
    private Long companyId;

}