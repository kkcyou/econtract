package com.yaoan.module.system.dal.mysql.user;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserExportReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {
    List<AdminUserDO> selectListXml(List<Long> userIds);

    default AdminUserDO selectByUsername(String username) {
        return selectOne(AdminUserDO::getUsername, username);
    }
    default AdminUserDO selectByAppOpenId(String openId) {
        return selectOne(AdminUserDO::getAppOpenId, openId);
    }

    default AdminUserDO selectByEmail(String email) {
        return selectOne(AdminUserDO::getEmail, email);
    }

    default AdminUserDO selectByMobile(String mobile) {
        return selectOne(AdminUserDO::getMobile, mobile);
    }

    default PageResult<AdminUserDO> selectPage(UserPageReqVO reqVO, Collection<Long> deptIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminUserDO>()
                .likeIfPresent(AdminUserDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AdminUserDO::getCompanyId, reqVO.getCompanyId())
                .betweenIfPresent(AdminUserDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserDO::getDeptId, deptIds)
                .orderByDesc(AdminUserDO::getId));
    }

    default List<AdminUserDO> selectList(UserExportReqVO reqVO, Collection<Long> deptIds) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .likeIfPresent(AdminUserDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminUserDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserDO::getDeptId, deptIds));
    }

    default List<AdminUserDO> selectListByNickname(String nickname) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>().like(AdminUserDO::getNickname, nickname));
    }

    default List<AdminUserDO> selectListByStatus(Integer status) {
        return selectList(AdminUserDO::getStatus, status);
    }

    default List<AdminUserDO> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(AdminUserDO::getDeptId, deptIds);
    }

    default List<AdminUserDO> selectListByDeptIdsAndUserIds(Collection<Long> deptIds, Set<Long> userIds) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .inIfPresent(AdminUserDO::getDeptId, deptIds)
                .inIfPresent(AdminUserDO::getId, userIds)
        );
    }

    default List<AdminUserDO> selectListByCompanyIdsAndRoleIds(List<Long> companyIds, List<Long> roleIds) {
        MPJLambdaWrapper<AdminUserDO> mpjLambdaWrapper = new MPJLambdaWrapper<AdminUserDO>()
                .selectAll(AdminUserDO.class).orderByDesc(AdminUserDO::getCreateTime);
        if (CollectionUtil.isNotEmpty(roleIds)) {
            mpjLambdaWrapper.leftJoin(UserRoleDO.class, UserRoleDO::getUserId, AdminUserDO::getId)
                    .in(UserRoleDO::getRoleId, roleIds)
            ;
        }
        if (CollectionUtil.isNotEmpty(companyIds)) {
            mpjLambdaWrapper.in(AdminUserDO::getCompanyId, companyIds);
        }
        return selectList(mpjLambdaWrapper);
    }




   default List<AdminUserDO> selectListByCompanyIds(List<Long> companyIds){
       MPJLambdaWrapper<AdminUserDO> mpjLambdaWrapper = new MPJLambdaWrapper<AdminUserDO>()
               .selectAll(AdminUserDO.class).orderByDesc(AdminUserDO::getCreateTime);
       if (CollectionUtil.isNotEmpty(companyIds)) {
           mpjLambdaWrapper.leftJoin(UserRoleDO.class, UserRoleDO::getUserId, AdminUserDO::getId)
                   .in(AdminUserDO::getCompanyId, companyIds)
           ;
       }
       return selectList(mpjLambdaWrapper);

   }

    default List<AdminUserDO> selectListByCompanyIdsAndUserIds(List<Long> companyIds, Set<Long> userIds){
        MPJLambdaWrapper<AdminUserDO> mpjLambdaWrapper = new MPJLambdaWrapper<AdminUserDO>()
                .selectAll(AdminUserDO.class).orderByDesc(AdminUserDO::getCreateTime);
        if (CollectionUtil.isNotEmpty(companyIds)) {
            mpjLambdaWrapper.leftJoin(UserRoleDO.class, UserRoleDO::getUserId, AdminUserDO::getId)
                    .in(AdminUserDO::getCompanyId, companyIds)
                    .in(AdminUserDO::getId,userIds)
            ;
        }
        return selectList(mpjLambdaWrapper);
    }

    default List<AdminUserDO> selectCompanyUserListByStatus(Integer status, Long companyId ) {
        LambdaQueryWrapperX<AdminUserDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(AdminUserDO::getCompanyId, companyId);
        wrapperX.eqIfPresent(AdminUserDO::getStatus, status);
        return selectList(wrapperX);
    }


}
