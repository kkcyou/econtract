package com.yaoan.module.system.api.permission;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.UserCompanyAllInfoRespDTO;
import com.yaoan.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.module.bpm.enums.ErrorCodeConstants.TASK_DELEGATE_FAIL_USER_NOT_EXISTS;

/**
 * 权限 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class PermissionApiImpl implements PermissionApi {

    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private CompanyApi companyApi;

    @Override
    public Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Set<Long> userIds = permissionService.getUserRoleIdListByRoleId(roleIds);
        //找出发起人同公司的审批人
        Set<Long> result = getAssigneeIdsInSameCompany(loginUserId, userIds);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptySet();
        }
        return result;
    }
    @Override
    public Set<Long> getUserRoleIdListByRoleIdsV2(Collection<Long> roleIds) {
        return permissionService.getUserRoleIdListByRoleIdV2(roleIds);
    }

    @Override
    public Set<Long> getUserRoleIdListByRoleIdsAndCompanyId(Collection<Long> roleIds,Long companyId) {
        return permissionService.getUserRoleIdListByRoleIdsAndCompanyId(roleIds, companyId);
    }

    /**
     * 获得 发起人同公司的特定角色的用户id
     *
     * @param submitterId 流程发起人
     * @param roleIds     审批节点的角色
     * @return
     */
    @Override
    public Set<Long> getApproverIdsBySubmitterAndRoleIds(Long submitterId, Collection<Long> roleIds) {
        Set<Long> userIds = permissionService.getUserRoleIdListByRoleId(roleIds);
        //找出发起人同公司的审批人
        Set<Long> result = getAssigneeIdsInSameCompany(submitterId, userIds);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptySet();
        }
        return result;
    }

    /**
     * public List<AdminUserRespDTO> selectListByDeptIdsAndUserIds(Collection<Long> deptIds, Set<Long> userIds)
     * 找出发起人同公司的审批人
     */
    public Set<Long> getAssigneeIdsInSameCompany(Long loginUserId, Set<Long> userIds) {
        //找到发起人的公司
        List<Long> loginUserIds = new ArrayList<Long>();
        loginUserIds.add(loginUserId);
        List<UserCompanyAllInfoRespDTO> companyInfoRespDTOS = companyApi.getUserCompanyAllInfoList(loginUserIds);
        if (CollectionUtils.isEmpty(companyInfoRespDTOS)) {
            return Collections.emptySet();
        }
        UserCompanyAllInfoRespDTO loginUserInfo = companyInfoRespDTOS.get(0);
        List<Long> companyIds = companyInfoRespDTOS.stream().filter(item -> loginUserInfo.getId().equals(item.getId())).map(UserCompanyAllInfoRespDTO::getId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(companyIds)) {
            throw exception(TASK_DELEGATE_FAIL_USER_NOT_EXISTS);
        }
        //从审批人中找出同公司的用户
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.selectListByCompanyIdsAndUserIds(companyIds,userIds);
        if (CollectionUtils.isEmpty(userRespDTOList)) {
            return Collections.emptySet();
        }
        return convertSet(userRespDTOList, AdminUserRespDTO::getId);
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return permissionService.hasAnyRoles(userId, roles);
    }

    @Override
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        return permissionService.getDeptDataPermission(userId);
    }

    @Override
    public Set<Long> getAssignIdsByTag(String textValue) {
        return null;
    }

    @Override
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        permissionService.assignUserRole(userId, roleIds);
    }
    @Override
    public void assignUserRole4Saas(Long userId, Set<Long> roleIds,String relativeId) {
        permissionService.assignUserRole4Saas(  userId,   roleIds,  relativeId);
    }
}
