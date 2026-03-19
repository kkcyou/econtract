package com.yaoan.module.econtract.dal.dataobject.warningdata;

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

import java.time.LocalDateTime;

/**
 * 预警结果 DO
 *
 * @author lls
 */
@TableName("ecms_warning_data")
@KeySequence("ecms_warning_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningDataDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 规则明细id
     */
    private String warningId;
    /**
     * 预警数据主键
     */
    private String warningDataId;
    /**
     * 预警数据值
     */
    private String warningDataValue;
    /**
     * 风险等级 低风险1 中风险2 高风险3
     */
    private Integer warningLevel;
    /**
     * 预警结果
     */
    private String warningResult;
    /**
     * 预警时间
     */
    private LocalDateTime warningDate;
    /**
     * 推送方式(多选),站内信message/短信sms/邮件email
     */
    private String pushType;
    /**
     * 短信模板渠道 发送短信的平台
     */
    private Integer pushChannel;
    /**
     * 推送用户集合
     */
    private String pushUser;
    /**
     * 所属公司
     */
    private Long companyId;

}