package com.yaoan.module.econtract.dal.dataobject.bpm.template;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 15:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_template_bpm")
public class TemplateBpmDO extends DeptBaseDO {

    private static final long serialVersionUID = -7632835790420809922L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 流程发起人
     */
    private Long userId;

    /**
     * 提交人名称
     */
    private String submitterName;

    /**
     * 范本id
     */
    private String templateId;


    /**
     * 范本名称
     */
    private String templateName;

    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 审批结果
     */
    private Integer result;

    /**
     * 原因
     */
    private String reason;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 租户编号
     */
    private Long tenantId;


}
