package com.yaoan.module.econtract.dal.dataobject.bpm.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 合同审批流程申请表
 * </p>
 *
 * @author doujiale
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_contract")
public class BpmContract extends DeptBaseDO implements Serializable  {


    private static final long serialVersionUID = -7045923172913492170L;

    /**
     * 合同审批流程表单主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 申请人的用户编号
     */
    private Long userId;

    /**
     * 合同id标识
     */
    private String contractId;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 申请结果
     */
    private Integer result;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 租户编号
     */
    private Long tenantId;

}
