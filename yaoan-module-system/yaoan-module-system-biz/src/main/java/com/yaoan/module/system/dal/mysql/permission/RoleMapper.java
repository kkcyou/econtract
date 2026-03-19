package com.yaoan.module.system.dal.mysql.permission;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleExportReqVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;

@Mapper
public interface RoleMapper extends BaseMapperX<RoleDO> {
//    @InterceptorIgnore(tenantLine = "true")
    default PageResult<RoleDO> selectPage(RolePageReqVO reqVO) {
        LambdaQueryWrapper<RoleDO> roleDOLambdaQueryWrapper = new LambdaQueryWrapperX<RoleDO>()
                .likeIfPresent(RoleDO::getName, reqVO.getName())
                .likeIfPresent(RoleDO::getCode, reqVO.getCode())
                .eqIfPresent(RoleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoleDO::getId);
//        if (reqVO.getType() == 1) {
//            //超管不做限制
//        } else if (reqVO.getType() == 2) {//是否是租户管理员查看整个租户
//            roleDOLambdaQueryWrapper.or(l -> l.eq(RoleDO::getTenantId, "0").
//                    or().eq(RoleDO::getTenantId, getLoginUser().getTenantId()));
//
//        } else
        if (reqVO.getType() == 3) {//其他 查询公共角色0和当前公司角色
            roleDOLambdaQueryWrapper.or(l -> l.eq(RoleDO::getCompanyId, "0")
                    .or().eq(getLoginUser().getCompanyId() > 0, RoleDO::getCompanyId, getLoginUser().getCompanyId()));
        }
        PageResult<RoleDO> roleDOPageResult = selectPage(reqVO, roleDOLambdaQueryWrapper);
        return roleDOPageResult;
    }

    default List<RoleDO> selectList(RoleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoleDO>()
                .likeIfPresent(RoleDO::getName, reqVO.getName())
                .likeIfPresent(RoleDO::getCode, reqVO.getCode())
                .eqIfPresent(RoleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime()));
    }

    default RoleDO selectByName(String name, Long companyId) {
        return selectOne(RoleDO::getName, name, RoleDO::getCompanyId, companyId);
    }

    default RoleDO selectByCode(String code, Long companyId) {
        return selectOne(RoleDO::getCode, code, RoleDO::getCompanyId, companyId);
    }

    default List<RoleDO> selectListByStatus(@Nullable Collection<Integer> statuses) {
        return selectList(RoleDO::getStatus, statuses);
    }

    default List<RoleDO> getRoleRespDTOByUserIds(List<Long> userIds) {
        MPJLambdaWrapper<RoleDO> mpjLambdaWrapper = new MPJLambdaWrapper<RoleDO>()
                .selectAll(RoleDO.class).orderByDesc(RoleDO::getUpdateTime)
                .leftJoin(UserRoleDO.class, UserRoleDO::getRoleId, RoleDO::getId);
        if (CollectionUtil.isNotEmpty(userIds)) {
            mpjLambdaWrapper.in(UserRoleDO::getUserId, userIds);
        }
        return selectList(mpjLambdaWrapper);
    }

    default List<RoleDO> selectByUserId(Long userId) {
        return selectList(new MPJLambdaWrapper<RoleDO>()
                .selectAll(RoleDO.class)
                .leftJoin(UserRoleDO.class, UserRoleDO::getRoleId, RoleDO::getId)
                .eq(UserRoleDO::getUserId, userId));
    }
}
