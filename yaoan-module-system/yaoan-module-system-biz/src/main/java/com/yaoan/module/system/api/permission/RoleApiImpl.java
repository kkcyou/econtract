package com.yaoan.module.system.api.permission;

import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.convert.permission.RoleConvert;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class RoleApiImpl implements RoleApi {

    @Resource
    private RoleService roleService;

    @Override
    public void validRoleList(Collection<Long> ids) {
        roleService.validateRoleList(ids);
    }

    @Override
    public List<RoleRespDTO> getRoleRespDTOByUserIds(List<Long> userIds) {
        return roleService.getRoleRespDTOByUserIds(userIds);
    }

    @Override
    public RoleRespDTO getRole(Long userId) {
        return roleService.getRoleRespDTOByUserId(userId);
    }
    @Override
    public List<RoleRespDTO> getRoles(Long userId) {
        return roleService.getRoleRespDTOSByUserId(userId);
    }

}
