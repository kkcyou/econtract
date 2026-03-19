package com.yaoan.module.econtract.dal.dataobject.code;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编号规则
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_code_rule")
public class CodeRuleDO extends TenantBaseDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 编号名称
     */
    private String name;

    /**
     * 编号规则内容
     */
    private String rule;

    /**
     * 状态 0：禁用，1：启用
     */
    private Integer status;

    /**
     * 是否预留 0 否 1 是
     */
    private Integer isReserve;
    /**
     * 引用模块
     */
    private String referenceModule;
    /**
     * 引用路径
     */
    private String referencePath;
}
