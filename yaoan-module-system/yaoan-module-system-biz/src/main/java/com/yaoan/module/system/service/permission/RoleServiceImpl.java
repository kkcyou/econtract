package com.yaoan.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.annotations.VisibleForTesting;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleExportReqVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import com.yaoan.module.system.convert.permission.RoleConvert;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.redis.RedisKeyConstants;
import com.yaoan.module.system.enums.permission.DataScopeEnum;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertList;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertMap;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 角色 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateReqVO reqVO, Integer type) {
        // 校验角色
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), null, getLoginUser().getCompanyId());
        // 插入到数据库
        RoleDO role = RoleConvert.INSTANCE.convert(reqVO);
        //添加所属公司
        Long companyId = reqVO.getCompanyId() != null && reqVO.getCompanyId() > 0 ? reqVO.getCompanyId() : getLoginUser().getCompanyId();
        role.setCompanyId(companyId);
        role.setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setDataScope(DataScopeEnum.ALL.getScope()); // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        roleMapper.insert(role);
        // 返回
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRoleWithTenantId(RoleCreateReqVO reqVO, Integer type, Long tenantId) {
        // 校验角色
        validateRoleDuplicateWithTenantId(reqVO.getName(), reqVO.getCode(), null, getLoginUser().getCompanyId(), tenantId);
        // 插入到数据库
        RoleDO role = RoleConvert.INSTANCE.convert(reqVO);
        role.setTenantId(tenantId);
        //添加所属公司
        Long companyId = reqVO.getCompanyId() != null && reqVO.getCompanyId() > 0 ? reqVO.getCompanyId() : getLoginUser().getCompanyId();
        role.setCompanyId(companyId);
        role.setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setDataScope(DataScopeEnum.ALL.getScope()); // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        roleMapper.insert(role);
        // 返回
        return role.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#reqVO.id")
    public void updateRole(RoleUpdateReqVO reqVO) {
        // 校验是否可以更新
        validateRoleForUpdate(reqVO.getId());
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), reqVO.getId(), getLoginUser().getCompanyId());

        // 更新到数据库
        RoleDO updateObj = RoleConvert.INSTANCE.convert(reqVO);
        roleMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleStatus(Long id, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新状态
        RoleDO updateObj = new RoleDO().setId(id).setStatus(status);
        roleMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        RoleDO updateObject = new RoleDO();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void deleteRole(Long id) {
        // 校验是否可以更新
        validateRoleForUpdate(id);
        // 标记删除
        roleMapper.deleteById(id);
        // 删除相关数据
        permissionService.processRoleDeleted(id);
    }

    /**
     * 校验角色的唯一字段是否重复
     * <p>
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id   角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id, Long companyId) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name, companyId);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code, companyId);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }
    @VisibleForTesting
    void validateRoleDuplicateWithTenantId(String name, String code, Long id, Long companyId, Long tenantId) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectOne(RoleDO::getName, name, RoleDO::getCompanyId, companyId, TenantBaseDO::getTenantId, tenantId);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectOne(RoleDO::getCode, code, RoleDO::getCompanyId, companyId, TenantBaseDO::getTenantId, tenantId);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleForUpdate(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

    @Override
    public RoleDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id",
            unless = "#result == null")
    public RoleDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public List<RoleDO> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleDO> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public List<RoleDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<RoleDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 这里采用 for 循环从缓存中获取，主要考虑 Spring CacheManager 无法批量操作的问题
        RoleServiceImpl self = getSelf();
        return convertList(ids, self::getRoleFromCache);
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO reqVO) {
        reqVO.setType(roleType(getLoginUserId()));
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public List<RoleDO> getRoleList(RoleExportReqVO reqVO) {
        return roleMapper.selectList(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();
        return ids.stream().anyMatch(id -> {
            RoleDO role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleDO> roles = roleMapper.selectBatchIds(ids);
        Map<Long, RoleDO> roleMap = convertMap(roles, RoleDO::getId);
        // 校验
        ids.forEach(id -> {
            RoleDO role = roleMap.get(id);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw exception(ROLE_IS_DISABLE, role.getName());
            }
        });
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }


    @Override
    public List<RoleRespDTO> getRoleRespDTOByUserIds(List<Long> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<RoleRespDTO> result = new ArrayList<RoleRespDTO>();
        List<RoleDO> roles = roleMapper.getRoleRespDTOByUserIds(userIds);
        if (CollectionUtil.isNotEmpty(roles)) {
            result = RoleConvert.INSTANCE.listEntity2DTO(roles);
        }
        return result;
    }

    @Override
    public RoleRespDTO getRoleRespDTOByUserId(Long userId) {
        String relativeId = redisUtils.get(SecurityFrameworkUtils.getLoginUserKey4Space());
        if(org.apache.commons.lang3.StringUtils.isBlank(relativeId)){
            List<UserRoleDO> roles = userRoleMapper.selectByUserId(userId);
            List<Long> collect = roles.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)){
                List<RoleDO> roleDO = roleMapper.selectList(RoleDO::getId, collect);
                List<RoleRespDTO> result = RoleConvert.INSTANCE.listEntity2DTO(roleDO);
                return result.get(0);
            }
        }
        List<UserRoleDO> roles = userRoleMapper.selectListByRelativeIdNUserId(relativeId,userId);
        if(CollectionUtil.isEmpty(roles)){
            return null;
        }
        RoleDO roleDO = roleMapper.selectById(roles.get(0).getRoleId());
        RoleRespDTO result = RoleConvert.INSTANCE.entity2DTO(roleDO);

        return result;
    }
    @Override
    public List<RoleRespDTO> getRoleRespDTOSByUserId(Long userId) {
        List<UserRoleDO> roles = userRoleMapper.selectByUserId(userId);
        List<Long> collect = roles.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(collect)){
            List<RoleDO> roleDO = roleMapper.selectList(RoleDO::getId, collect);
            List<RoleRespDTO> result = RoleConvert.INSTANCE.listEntity2DTO(roleDO);
            return result;
        }
        return new ArrayList<>();
    }

    /**
     * 是否是租户管理员
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isTenantAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        List<RoleDO> rolelist = roleMapper.selectByUserId(userId);
        if (rolelist == null || rolelist.size() == 0) {
            return false;
        }
        for (RoleDO roleDO : rolelist) {
            if (roleDO.getCode().equals(RoleCodeEnum.TENANT_ADMIN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前登录人的角色类型   1-超管 2-租户管理员  3-其他用户
     */
    @Override
    public Long roleType(Long userId) {
        if (userId == null) {
            return 0L;
        }
        List<RoleDO> rolelist = roleMapper.selectByUserId(userId);
        if (rolelist == null || rolelist.size() == 0) {
            return 0L;
        }
        for (RoleDO roleDO : rolelist) {
            if (roleDO.getCode().equals(RoleCodeEnum.TENANT_ADMIN.getCode())) {
                return 2L;
            } else if (roleDO.getCode().equals(RoleCodeEnum.SUPER_ADMIN.getCode())) {
                return 1L;
            }

        }
        return 3L;
    }

}
