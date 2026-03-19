package com.yaoan.module.econtract.dal.dataobject.alert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 15:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_alert")
public class AlertDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -3924144443120426751L;

    /**
     * 合同提醒主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 流程阶段
     */
    private String flowStage;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 业务id
     */
    private Integer businessId;

    /**
     * 业务名称
     */
    private String businessName;


}
