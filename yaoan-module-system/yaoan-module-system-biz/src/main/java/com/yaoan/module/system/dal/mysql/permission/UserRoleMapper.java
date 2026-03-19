package com.yaoan.module.system.dal.mysql.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {

    default List<UserRoleDO> selectListByUserId(Long userId) {
        return selectList(UserRoleDO::getUserId, userId);
    }

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, roleIds));
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, userId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRoleId, roleId));
    }

    default List<UserRoleDO> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(UserRoleDO::getRoleId, roleIds);
    }

    default List<UserRoleDO> getUserRoleIdListByRoleIdsAndCompanyId(Collection<Long> roleIds, Long companyId) {
        MPJLambdaWrapper<UserRoleDO> mpjLambdaWrapper = new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(UserRoleDO.class)
                .leftJoin(AdminUserDO.class, AdminUserDO::getId, UserRoleDO::getUserId)
                .in(UserRoleDO::getRoleId, roleIds)
                .eq(AdminUserDO::getCompanyId, companyId);
        return selectList(mpjLambdaWrapper);
    }

    default List<UserRoleDO> selectByUserId(Long userId) {
        MPJQueryWrapper<UserRoleDO> mpjQueryWrapper = new MPJQueryWrapper<UserRoleDO>()
                .eq("user_id", userId)
                .select("t.role_id")
                .leftJoin("system_role r on r.id=t.role_id");

        return selectList(mpjQueryWrapper);
    }

    default List<UserRoleDO> selectListByRoleIdsAndCompanyId(Collection<Long> roleIds) {
        MPJLambdaWrapper<UserRoleDO> wrapper=new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(UserRoleDO.class)
                .leftJoin(AdminUserDO.class,AdminUserDO::getId,UserRoleDO::getUserId)
                .selectAll(UserRoleDO.class)
                .in(UserRoleDO::getRoleId,roleIds)
                .eq(AdminUserDO::getCompanyId, SecurityFrameworkUtils.getLoginUserId())
                .distinct()
                ;
        return selectList(wrapper);
//        return selectList(UserRoleDO::getRoleId, roleIds);
    }

    default List<UserRoleDO> selectListByRelativeIdNUserId(String relativeId, Long userId){
        return selectList(new LambdaQueryWrapperX<UserRoleDO>().eq(UserRoleDO::getRelativeId, relativeId).eq(UserRoleDO::getUserId, userId));
    }

    default void deleteListByUserIdAndRelativeId(Long userId,  String relativeId){
        delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId)
                .eq(UserRoleDO::getRelativeId, relativeId)
        );
    }
}
