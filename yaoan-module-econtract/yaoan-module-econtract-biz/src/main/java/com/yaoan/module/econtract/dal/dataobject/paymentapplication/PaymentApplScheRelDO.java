package com.yaoan.module.econtract.dal.dataobject.paymentapplication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 付款申请和支付计划关系表
 * </p>
 *
 * @author Pele
 * @since 2024-01-05
 */
@Data
@TableName("ecms_payment_appl_sche_rel")
public class PaymentApplScheRelDO extends DeptBaseDO {

    private static final long serialVersionUID = 7192910526334491190L;
    /**
     * 主键 自增
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 付款申请id
     */
    @TableField("application_id")
    private String applicationId;

    /**
     * 支付id
     */
    @TableField("schedule_id")
    private String scheduleId;

    /**
     * 当前计划支付金额（元）
     */
    @TableField("current_pay_amount")
    private BigDecimal currentPayAmount;
}
