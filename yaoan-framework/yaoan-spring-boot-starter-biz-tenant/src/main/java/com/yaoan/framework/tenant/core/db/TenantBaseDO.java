package com.yaoan.framework.tenant.core.db;

import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展多租户的 BaseDO 基类
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {

    private static final long serialVersionUID = 7690244787738687138L;
    /**
     * 多租户编号
     */
    private Long tenantId;

}
