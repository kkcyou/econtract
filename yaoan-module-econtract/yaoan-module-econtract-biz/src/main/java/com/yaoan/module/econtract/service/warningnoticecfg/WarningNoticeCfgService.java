package com.yaoan.module.econtract.service.warningnoticecfg;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警通知配置表（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningNoticeCfgService {

    /**
     * 创建预警通知配置表（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningNoticeCfg(@Valid WarningNoticeCfgCreateReqVO createReqVO);

    /**
     * 更新预警通知配置表（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningNoticeCfg(@Valid WarningNoticeCfgUpdateReqVO updateReqVO);

    /**
     * 删除预警通知配置表（new预警）
     *
     * @param id 编号
     */
    void deleteWarningNoticeCfg(String id);

    /**
     * 获得预警通知配置表（new预警）
     *
     * @param id 编号
     * @return 预警通知配置表（new预警）
     */
    WarningNoticeCfgDO getWarningNoticeCfg(String id);

    /**
     * 获得预警通知配置表（new预警）列表
     *
     * @param ids 编号
     * @return 预警通知配置表（new预警）列表
     */
    List<WarningNoticeCfgDO> getWarningNoticeCfgList(Collection<String> ids);

    /**
     * 获得预警通知配置表（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警通知配置表（new预警）分页
     */
    PageResult<WarningNoticeCfgDO> getWarningNoticeCfgPage(WarningNoticeCfgPageReqVO pageReqVO);

    /**
     * 获得预警通知配置表（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警通知配置表（new预警）列表
     */
    List<WarningNoticeCfgDO> getWarningNoticeCfgList(WarningNoticeCfgExportReqVO exportReqVO);

}
