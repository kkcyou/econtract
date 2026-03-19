package com.yaoan.module.system.api.permission;

import com.yaoan.module.system.api.permission.dto.DeptDataPermissionRespDTO;

import java.util.Collection;
import java.util.Set;

/**
 * 权限 API 接口
 *
 * @author 芋道源码
 */
public interface PermissionApi {

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return
     */
    Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds);

    /**
     * 获得拥有多个角色的用户编号集合V2
     * 框架原本的角色分配方法
     * @param roleIds 角色编号集合
     * @return
     */
    Set<Long> getUserRoleIdListByRoleIdsV2(Collection<Long> roleIds);

    /**
     * 获得拥有多个角色的用户编号集合V2
     * 框架原本的角色分配方法
     * @param roleIds 角色编号集合
     * @return
     */
    Set<Long> getUserRoleIdListByRoleIdsAndCompanyId(Collection<Long> roleIds,Long companyId);
    /**
     * 获得 发起人同公司的特定角色的用户id
     * @param submitterId 流程发起人
     * @param roleIds 审批节点的角色
     * @return
     */
    public Set<Long> getApproverIdsBySubmitterAndRoleIds(Long submitterId, Collection<Long> roleIds);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(Long userId, String... permissions);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param userId 用户编号
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(Long userId, String... roles);

    /**
     * 获得登陆用户的部门数据权限
     *
     * @param userId 用户编号
     * @return 部门数据权限
     */
    DeptDataPermissionRespDTO getDeptDataPermission(Long userId);

    Set<Long> getAssignIdsByTag(String textValue);

    /**
     * assign roles to a user
     * */
    void assignUserRole(Long userId, Set<Long> roleIds);
    void assignUserRole4Saas(Long userId, Set<Long> roleIds,String relativeId);
}
