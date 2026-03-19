package com.yaoan.module.econtract.dal.mysql.fastentry;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.econtract.dal.dataobject.fastentry.UserMenuFastDO;
import org.apache.ibatis.annotations.Mapper;


import java.util.Collection;
import java.util.List;

@Mapper
public interface UserMenuFastMapper extends BaseMapperX<UserMenuFastDO> {

    default List<UserMenuFastDO> selectListByUserId(Long userId) {
        return selectList(UserMenuFastDO::getUserId, userId);
    }

    default void deleteListByUserIdAndMenuIdIds(Long userId, Collection<Long> menuIds) {
        delete(new LambdaQueryWrapper<UserMenuFastDO>()
                .eq(UserMenuFastDO::getUserId, userId)
                .in(UserMenuFastDO::getMenuId, menuIds));
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<UserMenuFastDO>().eq(UserMenuFastDO::getUserId, userId));
    }

    default void deleteListByMenuId(Long menuId) {
        delete(new LambdaQueryWrapper<UserMenuFastDO>().eq(UserMenuFastDO::getMenuId, menuId));
    }

    default List<UserMenuFastDO> selectListByMenuIds(Collection<Long> menuIds) {
        return selectList(UserMenuFastDO::getMenuId, menuIds);
    }

    default List<UserMenuFastDO> getUserMenuIdListByMenuIdsAndCompanyId(Collection<Long> menuIds, Long companyId) {
        MPJLambdaWrapper<UserMenuFastDO> mpjLambdaWrapper = new MPJLambdaWrapper<UserMenuFastDO>()
                .selectAll(UserMenuFastDO.class)
                .leftJoin(AdminUserDO.class, AdminUserDO::getId, UserMenuFastDO::getUserId)
                .in(UserMenuFastDO::getMenuId, menuIds)
                .eq(AdminUserDO::getCompanyId, companyId);
        return selectList(mpjLambdaWrapper);
    }

    default List<UserMenuFastDO> selectByUserId(Long userId) {
        MPJQueryWrapper<UserMenuFastDO> mpjQueryWrapper = new MPJQueryWrapper<UserMenuFastDO>()
                .eq("user_id", userId)
                .select("t.menu_id")
                .leftJoin("system_menu r on r.id=t.menu_id");

        return selectList(mpjQueryWrapper);
    }
}
