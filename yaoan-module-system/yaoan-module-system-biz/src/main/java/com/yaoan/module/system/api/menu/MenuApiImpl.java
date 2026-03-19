package com.yaoan.module.system.api.menu;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.system.api.menu.dto.MenuDTO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import com.yaoan.module.system.convert.auth.AuthConvert;
import com.yaoan.module.system.dal.dataobject.permission.MenuDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.permission.MenuMapper;
import com.yaoan.module.system.enums.permission.MenuTypeEnum;
import com.yaoan.module.system.service.permission.MenuService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.permission.RoleService;
import com.yaoan.module.system.service.user.AdminUserService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.framework.common.util.collection.CollectionUtils.filterList;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.system.dal.dataobject.permission.MenuDO.ID_ROOT;

@Service
public class MenuApiImpl implements MenuApi {

    @Resource
    private MenuService menuService;
    @Resource
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    @Override
    public List<MenuDTO> getMenuList(List<Long> menuIds) {
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<MenuDO> menuDOS = menuService.getMenuList(menuIds);
            return BeanUtils.toBean(menuDOS, MenuDTO.class);
        }
        return new ArrayList<>();
    }

    @Override
    public List<MenuDTO> getMenuList(Long size) {
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        if (user == null) {
            throw exception(UNAUTHORIZED);
        }

        // 1.2 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(getLoginUserId());
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色

        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, RoleDO::getId));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus())); // 移除禁用的菜单
        menuList.removeIf(menu-> !menuIds.contains(menu.getParentId()) && !ID_ROOT.equals(menu.getParentId()));
        Map<Long, Integer> sortMap = menuList.stream().collect(Collectors.toMap(MenuDO::getId, MenuDO::getSort));
        menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.BUTTON.getType()));
        menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.DIR.getType()));
        menuList.subList(0, Math.min(size.intValue(), menuList.size()));
        List<MenuDTO> menuDTOS = BeanUtils.toBean(menuList, MenuDTO.class);
        menuDTOS.forEach(menuDTO -> {
            menuDTO.setSort(sortMap.get(menuDTO.getId()));
        });
        return menuDTOS;
    }

    @Override
    public List<MenuDTO> getAllMenuList() {
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        if (user == null) {
            throw exception(UNAUTHORIZED);
        }

        // 1.2 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(getLoginUserId());
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色

        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, RoleDO::getId));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        Map<Long, Integer> sortMap = menuList.stream().collect(Collectors.toMap(MenuDO::getId, MenuDO::getSort));
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus())); // 移除禁用的菜单
        List<MenuDTO> menuDTOS = BeanUtils.toBean(menuList, MenuDTO.class);
        // 查出全部父级菜单
        List<MenuDTO> parentMenus = menuDTOS.stream().filter(menu -> ID_ROOT.equals(menu.getParentId())).collect(Collectors.toList());

        for (MenuDTO parentMenu : parentMenus) {
            findChildren(parentMenu, parentMenu.getId(), menuDTOS);
        }
        // 设置排序
        parentMenus.forEach(menuDTO -> {
            menuDTO.setSort(sortMap.get(menuDTO.getId()));
            if (CollectionUtil.isNotEmpty(menuDTO.getChildren())) {
                menuDTO.getChildren().sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
            }
        });
        parentMenus.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        return parentMenus;
    }

    private void findChildren(MenuDTO menuDTO, Long parentId, List<MenuDTO> menuDTOS) {
        List<MenuDTO> children = menuDTOS.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toList());
        for (MenuDTO child : children) {
            // 如果子菜单是菜单类型，则添加到子菜单集合
            if (MenuTypeEnum.MENU.getType().equals(child.getType())) {
                List<MenuDTO> menuDTOChildren = menuDTO.getChildren();
                if (menuDTOChildren == null) {
                    menuDTOChildren = new ArrayList<>();
                }
                menuDTOChildren.add(child);
                menuDTO.setChildren(menuDTOChildren);
            } else {
                //如果子菜单不是菜单类型，则递归查询子菜单
                findChildren(menuDTO, child.getId(), menuDTOS);
            }
        }
    }

}
