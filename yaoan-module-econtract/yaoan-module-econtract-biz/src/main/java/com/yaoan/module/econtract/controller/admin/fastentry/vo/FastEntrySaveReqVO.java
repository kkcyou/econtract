package com.yaoan.module.econtract.controller.admin.fastentry.vo;

import lombok.Data;

@Data
public class FastEntrySaveReqVO {
    /**
     * 快捷入口 菜单ID
     */
    private Long menuId;

    /**
     * 排序
     */
    private Long sort;
}
