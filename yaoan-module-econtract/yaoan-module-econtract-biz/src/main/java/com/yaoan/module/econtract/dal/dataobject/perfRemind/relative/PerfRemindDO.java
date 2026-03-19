package com.yaoan.module.econtract.dal.dataobject.perfRemind.relative;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_remind_set")
public class PerfRemindDO extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = 4487250800602777505L;
    /**
     * 相对方id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 提醒状态
     */
    private String status;


}
