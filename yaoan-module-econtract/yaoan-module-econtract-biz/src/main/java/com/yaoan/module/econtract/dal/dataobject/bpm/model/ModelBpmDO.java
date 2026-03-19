package com.yaoan.module.econtract.dal.dataobject.bpm.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Pele
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model_bpm")
public class ModelBpmDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -5517708803812674439L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 流程发起人
     */
    private Long userId;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 提交人名称
     */
    private String submitterName;

    /**
     * 模板名称
     */
    private String modelName;


    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 审批结果
     * {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 租户编号
     */
    private Long tenantId;



}
