package com.yaoan.module.system.api.tenant;

import com.yaoan.module.system.api.tenant.dto.TenantRespDTO;

import java.util.List;

/**
 * 多租户的 API 接口
 *
 * @author 芋道源码
 */
public interface TenantApi {

    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIdList();

    /**
     * 获得租户信息
     *
     * @return 租户信息
     */
    TenantRespDTO getTenantInfo(Long id);

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validateTenant(Long id);

    /**
     * 获得租户套餐信息
     * @param id 租户编号
     * @return 租户信息
     */
    Integer getTenantPackageType(Long id);

}
