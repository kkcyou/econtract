package com.yaoan.framework.tenant.core.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展部门的 BaseDO 基类
 *
 * @author doujl
 */

@EqualsAndHashCode(callSuper = true)
public abstract class DeptBaseDO extends TenantBaseDO {

    private static final long serialVersionUID = -5374829989887021253L;
    /**
     * 部门编号
     */
    @TableField(fill = FieldFill.INSERT)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
