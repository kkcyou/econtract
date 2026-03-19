package com.yaoan.module.econtract.dal.dataobject.bpm.register;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 11:15
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 合同等级审批流程申请表
 * </p>
 *
 * @author Pele
 * @since 2024-01-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_contract_register")
public class BpmContractRegisterDO extends DeptBaseDO {

    private static final long serialVersionUID = 3430452708492117661L;
    /**
     * 合同审批流程表单主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 申请人的用户编号
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 合同id标识
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 申请原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 申请结果
     */
    @TableField("result")
    private Integer result;

    /**
     * 流程实例的编号
     */
    @TableField("process_instance_id")
    private String processInstanceId;

}