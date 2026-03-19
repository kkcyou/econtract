package com.yaoan.module.system.service.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.StringUtils;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.config.TenantProperties;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.system.api.tenant.dto.TenantRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.yaoan.module.system.controller.admin.tenant.vo.tenant.*;
import com.yaoan.module.system.convert.tenant.TenantConvert;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import com.yaoan.module.system.dal.dataobject.permission.MenuDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.region.Region;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.dataobject.tenant.TenantDO;
import com.yaoan.module.system.dal.dataobject.tenant.TenantPackageDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import com.yaoan.module.system.dal.mysql.config.SystemConfigMapper;
import com.yaoan.module.system.dal.mysql.region.RegionMapper;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import com.yaoan.module.system.dal.mysql.tenant.TenantMapper;
import com.yaoan.module.system.dal.mysql.tenant.TenantPackageMapper;
import com.yaoan.module.system.dal.mysql.user.OrganizationMapper;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import com.yaoan.module.system.service.permission.MenuService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.permission.RoleService;
import com.yaoan.module.system.service.tenant.handler.TenantInfoHandler;
import com.yaoan.module.system.service.tenant.handler.TenantMenuHandler;
import com.yaoan.module.system.service.user.AdminUserService;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_START_USER_SELECT_ASSIGNEES_NOT_EXISTS;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singleton;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * 租户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // 由于 yudao.tenant.enable 配置项，可以关闭多租户的功能，所以这里只能不强制注入
    private TenantProperties tenantProperties;

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private TenantPackageMapper tenantPackageMapper;
    @Resource
    private TenantPackageService tenantPackageService;
    @Resource
//    @Lazy // 延迟，避免循环依赖报错
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SystemuserRelMapper systemuserRelMapper;
    @Resource
    private SystemConfigMapper systemConfigMapper;
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private RegionMapper regionMapper;



    @Override
    public List<Long> getTenantIdList() {
        List<TenantDO> tenants = tenantMapper.selectList();
        return CollectionUtils.convertList(tenants, TenantDO::getId);
    }

    @Override
    public void validTenant(Long id) {
        TenantDO tenant = getTenant(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        if (tenant.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_DISABLE, tenant.getName());
        }
        if (DateUtils.isExpired(tenant.getExpireTime())) {
            throw exception(TENANT_EXPIRE, tenant.getName());
        }
    }

    @Override
    public TenantRespDTO getTenantInfo(Long id) {
        TenantDO tenantDO = tenantMapper.selectById(id);
        return TenantConvert.INSTANCE.convert2DTO(tenantDO);
    }

    @Override
    public Integer getTenantPackageType(Long id) {
        TenantDO tenantDO = tenantMapper.selectById(id);
        TenantPackageDO tenantPackageDO = tenantPackageMapper.selectById(tenantDO.getPackageId());
        return tenantPackageDO != null ? tenantPackageDO.getType() : 0;
    }

    @Override
    @DSTransactional // 多数据源，使用 @DSTransactional 保证本地事务，以及数据源的切换
    public Long createTenant(TenantCreateReqVO createReqVO) {
        // 校验租户名称是否重复
        validTenantNameDuplicate(createReqVO.getName(), null);
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(createReqVO.getPackageId());
        // 创建租户
        TenantDO tenant = TenantConvert.INSTANCE.convert(createReqVO);
        tenantMapper.insert(tenant);
        // 关联单位
        // 查询该单位是否关联单位
        String orgId = getString(createReqVO, tenant);
        SystemuserRelDO systemuserRelDO = new SystemuserRelDO();
        systemuserRelDO.setBuyerOrgId(orgId);
        systemuserRelDO.setCurrentTenantId(tenant.getId());
        systemuserRelMapper.insert(systemuserRelDO);
        TenantUtils.execute(tenant.getId(), () -> {
            // 创建角色 分配角色菜单
            Long roleId = createRole(tenantPackage);
            // 创建用户，并分配角色
            Long userId = createUser(roleId, createReqVO);
            // 修改租户的管理员
            tenantMapper.updateById(new TenantDO().setId(tenant.getId()).setContactUserId(userId));
        });
        return tenant.getId();
    }
    @Override
    @Transactional
    public Map createTenantForOrg(TenantCreateReqVO createReqVO) {
        // 校验租户名称是否重复
        validTenantNameDuplicate(createReqVO.getName(), null);
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(createReqVO.getPackageId());
        // 创建租户
        TenantDO tenant = TenantConvert.INSTANCE.convert(createReqVO);
        tenantMapper.insert(tenant);
        // 关联单位
        // 查询该单位是否关联单位
        Map map = new HashMap();
        map.put("tenantId", tenant.getId());
        map.put("tenantPackage", tenantPackage);
        return map;
    }

    private  String modelCountControl(TenantBaseVO reqVO) {
        int goodsCount = reqVO.getGoodsCount() != null ? reqVO.getGoodsCount() : 0;
        int serviceCount = reqVO.getServiceCount() != null ? reqVO.getServiceCount() : 0;
        int projectCount = reqVO.getProjectCount() != null ? reqVO.getProjectCount() : 0;
        int otherCount = reqVO.getOtherCount() != null ? reqVO.getOtherCount() : 0;
        //将数量按照货物、服务、工程、非政采的顺序拼接，中间用,隔开
        return goodsCount + "," + serviceCount + "," + projectCount + "," + otherCount;
    }

    private Long createUser(Long roleId, TenantCreateReqVO createReqVO) {
        // 创建用户
        Long userId = userService.createUser(TenantConvert.INSTANCE.convert02(createReqVO));
        // 分配角色
        permissionService.assignUserRole(userId, singleton(roleId));
        return userId;
    }

    private Long createRole(TenantPackageDO tenantPackage) {
        // 创建角色
        RoleCreateReqVO reqVO = new RoleCreateReqVO();
        reqVO.setName(RoleCodeEnum.TENANT_ADMIN.getName()).setCode(RoleCodeEnum.TENANT_ADMIN.getCode())
                .setSort(0).setRemark("系统自动生成");
        Long roleId = roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
        // 分配权限
        permissionService.assignRoleMenu(roleId, tenantPackage.getMenuIds());
        return roleId;
    }



    @Override
    @DSTransactional
    public void updateTenant(TenantUpdateReqVO updateReqVO) {
        // 校验存在
        TenantDO tenant = validateUpdateTenant(updateReqVO.getId());
        // 校验租户名称是否重复
        validTenantNameDuplicate(updateReqVO.getName(), updateReqVO.getId());
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(updateReqVO.getPackageId());

        // 更新租户
        TenantDO updateObj = TenantConvert.INSTANCE.convert(updateReqVO);
        tenantMapper.updateById(updateObj);
        //更新关联单位
        //查询该单位是否关联租户
        String orgId = getString(updateReqVO, tenant);
        SystemuserRelDO systemuserRelDO = new SystemuserRelDO();
        systemuserRelDO.setBuyerOrgId(orgId);
        systemuserRelMapper.update(systemuserRelDO,new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getCurrentTenantId, tenant.getId()));
        //更新密码
        TenantUtils.execute(tenant.getId(), () -> {
            if(ObjectUtil.isNotEmpty(updateReqVO.getUserId()) && ObjectUtil.isNotEmpty(updateReqVO.getPassword())){
                userService.updateUserPassword(updateReqVO.getUserId(), updateReqVO.getPassword());
            }
            //设置模板数量
            String value = modelCountControl(updateReqVO);
            List<SystemConfigDO> systemConfigDOS = systemConfigMapper.selectList(new LambdaQueryWrapperX<SystemConfigDO>()
                    .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey())
                    .eq(SystemConfigDO::getTenantId, updateReqVO.getId()));
            if(CollectionUtil.isNotEmpty(systemConfigDOS)){
                systemConfigMapper.updateById((SystemConfigDO) new SystemConfigDO().setId(systemConfigDOS.get(0).getId()).setCValue(value).setTenantId(updateReqVO.getId()));
            } else {
                systemConfigMapper.insert((SystemConfigDO) new SystemConfigDO().setCKey(SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey()).setCValue(value).setTenantId(updateReqVO.getId()));
            }
        });

        // 如果套餐发生变化，则修改其角色的权限
        if (ObjectUtil.notEqual(tenant.getPackageId(), updateReqVO.getPackageId())) {
            updateTenantRoleMenu(tenant.getId(), tenantPackage.getMenuIds());
        }
    }
    @Override
    @DSTransactional
    public void updateTenantSetting(TenantUpdateReqVO updateReqVO) {
        // 校验存在
        TenantDO tenant = validateUpdateTenant(updateReqVO.getId());
//       
        TenantUtils.execute(tenant.getId(), () -> {
            //设置模板数量
            String value = modelCountControl(updateReqVO);
            List<SystemConfigDO> systemConfigDOS = systemConfigMapper.selectList(new LambdaQueryWrapperX<SystemConfigDO>()
                    .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey())
                    .eq(SystemConfigDO::getTenantId, updateReqVO.getId()));
            if(CollectionUtil.isNotEmpty(systemConfigDOS)){
                systemConfigMapper.updateById((SystemConfigDO) new SystemConfigDO().setId(systemConfigDOS.get(0).getId()).setCValue(value).setTenantId(updateReqVO.getId()));
            } else {
                systemConfigMapper.insert((SystemConfigDO) new SystemConfigDO().setCKey(SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey()).setCValue(value).setTenantId(updateReqVO.getId()));
            }
        });
    }
    private String getString(TenantBaseVO updateReqVO, TenantDO tenant) {
        List<String> orgIds = updateReqVO.getOrgId();
        // 取数组最后一个
        String orgId = orgIds.get(orgIds.size() - 1);
        OrganizationDO organizationDO = organizationMapper.selectById(orgId);
        if(ObjectUtil.isEmpty(organizationDO)){
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"该单位不存在，请重新选择");
        }
        Long l = systemuserRelMapper.selectCount(new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getBuyerOrgId, orgId).ne(SystemuserRelDO::getCurrentTenantId, tenant.getId()));
        if(l > 0){
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"该单位已关联租户，不可修改");
        }
        return orgId;
    }

    private void validTenantNameDuplicate(String name, Long id) {
        TenantDO tenant = tenantMapper.selectByName(name);
        if (tenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同名字的租户
        if (id == null) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
        if (!tenant.getId().equals(id)) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
    }

    @Override
    @DSTransactional
    public void updateTenantRoleMenu(Long tenantId, Set<Long> menuIds) {
        TenantUtils.execute(tenantId, () -> {
            // 获得所有角色
            List<RoleDO> roles = roleService.getRoleListByStatus(Collections.singleton(0));
            roles.forEach(role -> Assert.isTrue(tenantId.equals(role.getTenantId()), "角色({}/{}) 租户不匹配",
                    role.getId(), role.getTenantId(), tenantId)); // 兜底校验
            // 重新分配每个角色的权限
            roles.forEach(role -> {
                // 如果是租户管理员，重新分配其权限为租户套餐的权限
                if (Objects.equals(role.getCode(), RoleCodeEnum.TENANT_ADMIN.getCode())) {
                    permissionService.assignRoleMenu(role.getId(), menuIds);
                    log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), menuIds);
                    return;
                }
                // 如果是其他角色，则去掉超过套餐的权限
                Set<Long> roleMenuIds = permissionService.getRoleMenuListByRoleId(role.getId());
                roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIds);
                permissionService.assignRoleMenu(role.getId(), roleMenuIds);
                log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), roleMenuIds);
            });
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTenant(Long id) {
        // 校验存在
        validateUpdateTenant(id);
        // 删除
        tenantMapper.deleteById(id);
        // 同时删除租户和单位的关联
        systemuserRelMapper.delete(new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getCurrentTenantId, id));
    }

    private TenantDO validateUpdateTenant(Long id) {
        TenantDO tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw exception(TENANT_CAN_NOT_UPDATE_SYSTEM);
        }
        return tenant;
    }

    @Override
    public TenantDO getTenant(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public PageResult<TenantDO> getTenantPage(TenantPageReqVO pageReqVO) {
        return tenantMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TenantDO> getTenantList(TenantExportReqVO exportReqVO) {
        return tenantMapper.selectList(exportReqVO);
    }

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }

    @Override
    public Long getTenantCountByPackageId(Long packageId) {
        return tenantMapper.selectCountByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByPackageId(Long packageId) {
        return tenantMapper.selectListByPackageId(packageId);
    }

    @Override
    public void handleTenantInfo(TenantInfoHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        // 执行处理器
        handler.handle(tenant);
    }

    @Override
    public void handleTenantMenu(TenantMenuHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户，然后获得菜单
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        Set<Long> menuIds;
        if (isSystemTenant(tenant)) { // 系统租户，菜单是全量的
            menuIds = CollectionUtils.convertSet(menuService.getMenuList(), MenuDO::getId);
        } else {
            menuIds = tenantPackageService.getTenantPackage(tenant.getPackageId()).getMenuIds();
        }
        // 执行处理器
        handler.handle(menuIds);
    }

    private static boolean isSystemTenant(TenantDO tenant) {
        return Objects.equals(tenant.getPackageId(), TenantDO.PACKAGE_ID_SYSTEM);
    }

    private boolean isTenantDisable() {
        return tenantProperties == null || Boolean.FALSE.equals(tenantProperties.getEnable());
    }

    @Override
    public TenantDO getTenantByWebsite(String website) {
        return tenantMapper.selectByWebsite(website);
    }

    @Override
    public TenantRespVO getTenantRespVO(Long id) {
        TenantDO tenantDO = tenantMapper.selectById(id);
        TenantRespVO convert = TenantConvert.INSTANCE.convert(tenantDO);
        if (tenantDO != null) {
            DataPermissionUtils.executeIgnore(()->{
                TenantUtils.executeIgnore(()->{
                    AdminUserDO user = userService.getUser(tenantDO.getContactUserId());
                    if(user != null){
                        convert.setUserId(String.valueOf(user.getId()));
                        convert.setUsername(user.getUsername());
                    }
                    //获取单位id
                    List<SystemuserRelDO> systemUserRelDOS = systemuserRelMapper.selectList(new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getCurrentTenantId, id));
                    if(CollectionUtil.isNotEmpty(systemUserRelDOS)){
                        List<String> orgId = new ArrayList<>();
                        OrganizationDO organizationDO = organizationMapper.selectById(systemUserRelDOS.get(0).getBuyerOrgId());
                        Region region = regionMapper.selectById(organizationDO.getRegionGuid());
                        if (ObjectUtil.isNotEmpty(region)) {
                            // 获取区划及其所有父级区划
                            List<String> regionHierarchy = getRegionHierarchy(region.getRegionGuid());
                            orgId.addAll(regionHierarchy);
                        }

                        if (ObjectUtil.isNotEmpty(organizationDO)) {
                            orgId.add(organizationDO.getId());
                        }
                        convert.setOrgId(orgId);
                    }
                    List<SystemConfigDO> systemConfigDOS = systemConfigMapper.selectList(new LambdaQueryWrapperX<SystemConfigDO>()
                            .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey())
                            .eq(SystemConfigDO::getTenantId, id));
                    // 模板数量
                    if(CollectionUtil.isNotEmpty(systemConfigDOS)){
                        // 将CValue按照货物、服务、工程、非政采的顺序拼拆分，分别保存在TenantRespVO中
                        String[] split = systemConfigDOS.get(0).getCValue().split(",");
                        convert.setGoodsCount(Integer.valueOf(split[0]));
                        convert.setServiceCount(Integer.valueOf(split[1]));
                        convert.setProjectCount(Integer.valueOf(split[2]));
                        convert.setOtherCount(Integer.valueOf(split[3]));

                    }else{
                        convert.setGoodsCount(0);
                        convert.setServiceCount(0);
                        convert.setProjectCount(0);
                        convert.setOtherCount(0);
                    }

                });
            });
            return convert;
        }
        return null;
    }
    public List<String> getRegionHierarchy(String regionGuid) {
        List<String> regionHierarchy = new ArrayList<>();
        // 递归查询区划父级
        addRegionHierarchy(regionGuid, regionHierarchy);
        return regionHierarchy;
    }

    private void addRegionHierarchy(String regionGuid, List<String> regionHierarchy) {
        // 获取当前区划信息
        Region region = regionMapper.selectById(regionGuid);

        // 如果区划存在并且父级区划不为空
        if (region != null) {
            // 如果父级区划是根区划或者没有父级区划，直接结束递归并添加当前区划
            if ("0".equals(region.getRegionParentGuid()) || StringUtils.isEmpty(region.getRegionParentGuid())) {
                regionHierarchy.add(region.getRegionGuid());
            } else {
                // 否则，先递归查询父级区划
                addRegionHierarchy(region.getRegionParentGuid(), regionHierarchy);
                // 再添加当前区划
                regionHierarchy.add(region.getRegionGuid());
            }
        }
    }

}
