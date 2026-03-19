package com.yaoan.module.system.api.permission;

import com.yaoan.module.system.api.permission.dto.RoleRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色 API 接口
 *
 * @author 芋道源码
 */
public interface RoleApi {

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    void validRoleList(Collection<Long> ids);

    List<RoleRespDTO> getRoleRespDTOByUserIds(List<Long> userIds);

    /**
     * 获得角色
     *
     * @param userId 角色编号
     * @return 角色
     */
    RoleRespDTO getRole(Long userId);
    List<RoleRespDTO> getRoles(Long userId);

}
