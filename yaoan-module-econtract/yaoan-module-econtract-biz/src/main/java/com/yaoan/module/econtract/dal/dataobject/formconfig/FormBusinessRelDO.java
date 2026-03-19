package com.yaoan.module.econtract.dal.dataobject.formconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/18 19:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_form_business_rel")
public class FormBusinessRelDO  extends DeptBaseDO {

    private static final long serialVersionUID = 638443130458803835L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 业务id
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 业务id
     */
    @TableField("form_id")
    private String formId;

    /**
     * 状态 0=停用 1=启用
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
