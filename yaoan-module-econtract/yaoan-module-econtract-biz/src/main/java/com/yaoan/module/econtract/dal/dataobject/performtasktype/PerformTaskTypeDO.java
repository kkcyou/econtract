package com.yaoan.module.econtract.dal.dataobject.performtasktype;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_perform_task_type")
public class PerformTaskTypeDO extends TenantBaseDO {

    private static final long serialVersionUID = 1202286608519812246L;

    /**
     * 履约任务类型id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 履约任务类型编码
     */
    private String code;

    /**
     * 履约任务类型名称
     */
    private String name;

    /**
     * 是否可更改
     */
    private Boolean modifiable;

}
