package com.yaoan.module.econtract.dal.dataobject.riskalert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/18 21:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_risk_alert")
public class RiskAlertDO extends DeptBaseDO {

    private static final long serialVersionUID = -8738025871512491333L;

    /**
     * 风险提示ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 风险名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 风险类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 履约任务ID
     */
    @TableField(value = "perform_task_id")
    private String performTaskId;

    /**
     * 履约任务创建者
     */
    @TableField(value = "perform_task_creator")
    private String performTaskCreator;

    /**
     * 相对方ID
     */
    @TableField(value = "relative_id")
    private String relativeId;

    /**
     * 是否提示过
     */
    @TableField(value = "alerted")
    private Boolean alerted;

    /**
     * 提示时间
     */
    @TableField(value = "alert_time")
    private LocalDateTime alertTime;

    /**
     * 合同名称
     */
    @TableField(value = "contract_name")
    private String contractName;

    /**
     * 合同编号
     */
    @TableField(value = "contract_code")
    private String contractCode;
}
