package com.yaoan.module.econtract.service.fastentry;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryCreateReqVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryDetailRespVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryRespVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntrySelVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchRespVO;
import com.yaoan.module.econtract.dal.dataobject.fastentry.UserMenuFastDO;
import com.yaoan.module.econtract.dal.mysql.fastentry.UserMenuFastMapper;
import com.yaoan.module.econtract.service.roleworkbenchrel.RoleWorkbenchRelService;
import com.yaoan.module.system.api.menu.MenuApi;
import com.yaoan.module.system.api.menu.dto.MenuDTO;
import com.yaoan.module.system.controller.admin.permission.vo.menu.MenuRespVO;
import com.yaoan.module.system.dal.dataobject.permission.MenuDO;
import com.yaoan.module.system.dal.mysql.permission.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
@Validated
@Slf4j
public class FastEntryServiceImpl implements FastEntryService{

    @Resource
    private UserMenuFastMapper userMenuFastMapper;

    @Resource
    private MenuApi menuApi;

    @Override
    public void saveFastEntryData(FastEntryCreateReqVO createReqVO) {

        // 查询原来的配置并删除
        userMenuFastMapper.deleteListByUserId(getLoginUserId());

        // 保存新的配置信息
        List<UserMenuFastDO> userMenuFastDOList = new ArrayList<>();
        createReqVO.getItems().forEach(item -> {
            UserMenuFastDO userMenuFastDO = BeanUtils.toBean(item, UserMenuFastDO.class);
            userMenuFastDO.setUserId(getLoginUserId());
            userMenuFastDOList.add(userMenuFastDO);
        });
        userMenuFastMapper.insertBatch(userMenuFastDOList);
    }

    @Override
    public FastEntryRespVO getUserFastEntryData(FastEntrySelVO getReq) {
        // 如果没有指定返回菜单数量则默认返回8个
        Long size = ObjectUtil.isEmpty(getReq.getSize())?8:getReq.getSize();
        List<UserMenuFastDO> userMenuFastDOS = userMenuFastMapper.selectList(UserMenuFastDO::getUserId, getLoginUserId());
        List<FastEntryDetailRespVO> resultList = new ArrayList<>();
        List<MenuDTO> menuList = new ArrayList<>();
        Map<Long, Object> menuSortMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userMenuFastDOS)){
            menuSortMap = userMenuFastDOS.stream().collect(Collectors.toMap(UserMenuFastDO::getMenuId, UserMenuFastDO::getSort));
            List<Long> menuIds = userMenuFastDOS.stream().map(UserMenuFastDO::getMenuId).collect(Collectors.toList());
            menuList = menuApi.getMenuList(menuIds);
        } else {
            // 如果查询配置的菜单为空，则从菜单列表中按顺序取出指定数量的菜单
            menuList = menuApi.getMenuList(size);
            menuSortMap = menuList.stream().collect(Collectors.toMap(MenuDTO::getId, MenuDTO::getSort));
        }
        if (CollectionUtil.isNotEmpty(menuList)){
            List<Long> parentIds = menuList.stream().map(MenuDTO::getParentId).collect(Collectors.toList());
            List<MenuDTO> parentMenus = new ArrayList<>();
            getAllParentMenus(parentIds, parentMenus);
            Map<Long, MenuDTO> parentPathMap = parentMenus.stream().distinct().filter(item->StringUtils.isNotEmpty(item.getPath())).collect(Collectors.toMap(MenuDTO::getId, item->item));

            for (MenuDTO menuDO : menuList) {
                MenuRespVO menuRespVO = BeanUtils.toBean(menuDO, MenuRespVO.class);
                contactParentPath(menuRespVO, menuDO.getParentId(), parentPathMap);
                FastEntryDetailRespVO fastEntryDetailRespVO = new FastEntryDetailRespVO();
                resultList.add(fastEntryDetailRespVO.setMenu(menuRespVO).setSort(Long.valueOf(menuSortMap.get(menuDO.getId()).toString())));
            }
        }
        resultList = resultList.subList(0, Math.min(size.intValue(), resultList.size()));
        resultList.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        return new FastEntryRespVO().setItems(resultList);
    }


    @Override
    public FastEntryRespVO getFastEntryAllMenus() {
        List<MenuDTO> parentMenus = menuApi.getAllMenuList();
        List<FastEntryDetailRespVO> items = new ArrayList<>();
        for (MenuDTO parentMenu : parentMenus) {
            List<MenuDTO> children = parentMenu.getChildren();

            List<MenuDTO> parMenus = new ArrayList<>();
            if (CollectionUtil.isEmpty(children)) {
                children = new ArrayList<>();
            }
            List<Long> collect = children.stream().map(MenuDTO::getParentId).collect(Collectors.toList());
            getAllParentMenus(collect, parMenus);
            Map<Long, MenuDTO> parentPathMap = parMenus.stream().distinct().filter(item->StringUtils.isNotEmpty(item.getPath())).collect(Collectors.toMap(MenuDTO::getId, item->item));

            List<MenuRespVO> respVOList = new ArrayList<>();
            for (MenuDTO menuDO : children) {
                MenuRespVO menuRespVO = BeanUtils.toBean(menuDO, MenuRespVO.class);
                contactParentPath(menuRespVO, menuDO.getParentId(), parentPathMap);
                respVOList.add(menuRespVO);
            }

            FastEntryDetailRespVO fastEntryDetailRespVO= new FastEntryDetailRespVO();
            fastEntryDetailRespVO.setMenu(BeanUtils.toBean(parentMenu, MenuRespVO.class));
            fastEntryDetailRespVO.setChildren(respVOList);
            items.add(fastEntryDetailRespVO);
        }
        return new FastEntryRespVO().setItems(items);
    }


    private void contactParentPath(MenuRespVO menuDO, Long parentId, Map<Long, MenuDTO>  parentPathMap){
        // 组装菜单返回的路径。菜单以”/“开头则不需要拼接，否则拼接上父级菜单的path
        MenuDTO menuDTO = parentPathMap.get(parentId);
        if (ObjectUtil.isNotEmpty(menuDTO)){
            if (StringUtils.isNotEmpty(menuDO.getPath()) && !"/".equals(menuDO.getPath().substring(0, 1))){
                menuDO.setPath( menuDTO.getPath() + "/" + menuDO.getPath());
            }
            // 如果菜单的父级菜单不是根菜单，则递归调用，继续组装父级菜单的路径
            if (!Long.valueOf("0").equals(menuDTO.getParentId())){
                contactParentPath(menuDO, menuDTO.getParentId(), parentPathMap);
            }
        }
    }

    private void getAllParentMenus(List<Long> parentIds, List<MenuDTO> menuList){
        List<MenuDTO> parentMenus = menuApi.getMenuList(parentIds);
        menuList.addAll(parentMenus);
        List<Long> ids = parentMenus.stream().filter(menuDTO -> !Long.valueOf("0").equals(menuDTO.getParentId())).map(MenuDTO::getParentId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ids)){
            getAllParentMenus(ids, menuList);
        }
    }
}
