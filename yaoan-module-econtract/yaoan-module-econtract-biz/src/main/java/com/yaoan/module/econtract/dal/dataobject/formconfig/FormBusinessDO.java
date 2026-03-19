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
 * @date: 2024/3/18 19:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_form_business")
public class FormBusinessDO extends DeptBaseDO {

    private static final long serialVersionUID = 6650839766919873136L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

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

}
