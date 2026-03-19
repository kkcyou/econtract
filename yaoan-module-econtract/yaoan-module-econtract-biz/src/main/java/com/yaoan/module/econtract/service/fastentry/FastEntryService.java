package com.yaoan.module.econtract.service.fastentry;

import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryCreateReqVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryRespVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntrySelVO;

public interface FastEntryService {
    /**
     * 保存用户的快捷菜单
      */
    void saveFastEntryData(FastEntryCreateReqVO createReqVO);

    /**
     * 查看当前用户配置的快捷菜单
     */
    FastEntryRespVO getUserFastEntryData(FastEntrySelVO getReq);

    /**
     * 查看所有菜单(只含第一级目录和该目录下的所有菜单，不包含中间目录)
     */
    FastEntryRespVO getFastEntryAllMenus();
}
