package com.yaoan.module.econtract.controller.admin.fastentry.vo;

import lombok.Data;

import java.util.List;

@Data
public class FastEntryCreateReqVO {

    /**
     * 快捷入口 菜单ID
     */
    private List<FastEntrySaveReqVO> items;
}
