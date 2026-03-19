package com.yaoan.module.system.api.menu;

import com.yaoan.module.system.api.menu.dto.MenuDTO;

import java.util.List;

public interface MenuApi {
    /**
     * 按照指定菜单id集合，取出菜单集合
     *
     * @param menuIds 菜单id集合
     */
    List<MenuDTO> getMenuList(List<Long> menuIds);

    /**
     * 取出全部菜单集合
     */
    List<MenuDTO> getAllMenuList();

    /**
     * 按照指定数量，取出菜单集合
     *
     * @param size 菜单数量
     */
    List<MenuDTO> getMenuList(Long size);
}
