package com.yaoan.module.system.api.user;

import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;

import java.util.*;

/**
 * Admin 用户 API 接口
 *
 * @author 芋道源码
 */
public interface AdminUserApi {
    /**
     * 通过模糊查询昵称找到users
     */
    List<AdminUserRespDTO> getUserListLikeNickname(String nickname);


    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    AdminUserRespDTO getUser(Long id);


    /**
     * 通过用户 ID 查询用户们
     *
     * @param ids 用户 ID 们
     * @return 用户对象信息
     */
    List<AdminUserRespDTO> getUserList(Collection<Long> ids);
    List<AdminUserRespDTO> getUserList2(List<Long> ids);
    /**
     * 获得指定部门的用户数组
     *
     * @param deptIds 部门数组
     * @return 用户数组
     */
    List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * 获得指定岗位的用户数组
     *
     * @param postIds 岗位数组
     * @return 用户数组
     */
    List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, AdminUserRespDTO> getUserMap(Collection<Long> ids) {
        List<AdminUserRespDTO> users = getUserList(ids);
        return CollectionUtils.convertMap(users, AdminUserRespDTO::getId);
    }

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    void validateUserList(Collection<Long> ids);

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     */
    AdminUserRespDTO selectAdminUser(String account, String name, String idCard);

    /**
     * 根据用户id获取用户对应的部门和公司信息
     */
    UserCompanyDeptRespDTO selectUserCompanyDept(Long userId);

    /**
     * 获取所有用户信息
     */
    List<AdminUserRespDTO> getUserList();

    /**
     * 获取所有用户信息
     */
    AdminUserRespDTO getUserByUsername(String username);

    List<String> getUserIdsByNameLike(String searchText);

    /**
     * 从审批人中找出同部门的用户
     */
    List<AdminUserRespDTO> selectListByDeptIdsAndUserIds(List<Long> deptIds, Set<Long> userIds);

    Map<Long, AdminUserRespDTO> getUserRoleInfos(List<Long> userIds);

    List<AdminUserRespDTO> selectListByCompanyIdsAndRoleIds(List<Long> companyIds, List<Long> roleIds);


    public List<AdminUserRespDTO> selectListByCompanyIdsSaas(List<Long> companyIds);

    List<AdminUserRespDTO> selectListByCompanyIdsAndUserIds(List<Long> companyIds, Set<Long> userIds);

    /**
     * 获取用户昵称
     * @param userIds
     * @return
     */
    List<AdminUserRespDTO> getUserNickList(List<Long> userIds);

    String getUserNickById(Long valueOf);

    List<AdminUserRespDTO> getUserList4Tel(List<String> telList);

    List<Long> saveBatch(List<UserDTO> userDTOList);

    void updateUsers(List<UserDTO> userDTOList);


}
