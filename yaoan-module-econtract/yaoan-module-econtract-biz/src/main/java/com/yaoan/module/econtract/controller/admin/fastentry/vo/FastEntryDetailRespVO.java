package com.yaoan.module.econtract.controller.admin.fastentry.vo;

import com.yaoan.module.system.controller.admin.permission.vo.menu.MenuRespVO;
import lombok.Data;

import java.util.List;

@Data
public class FastEntryDetailRespVO {

    private MenuRespVO menu;
    private Long sort;
    private List<MenuRespVO> children;

}
