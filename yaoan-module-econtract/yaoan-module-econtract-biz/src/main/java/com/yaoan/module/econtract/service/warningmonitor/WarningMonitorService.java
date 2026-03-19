package com.yaoan.module.econtract.service.warningmonitor;

import java.util.*;
import javax.validation.*;

import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.warningmonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警监控项配置表（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningMonitorService {

    /**
     * 创建预警监控项配置表（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningMonitor(@Valid WarningMonitorCreateReqVO createReqVO);

    /**
     * 更新预警监控项配置表（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningMonitor(@Valid WarningMonitorUpdateReqVO updateReqVO);

    /**
     * 删除预警监控项配置表（new预警）
     *
     * @param id 编号
     */
    void deleteWarningMonitor(String id);

    /**
     * 获得预警监控项配置表（new预警）
     *
     * @param id 编号
     * @return 预警监控项配置表（new预警）
     */
    WarningMonitorDO getWarningMonitor(String id);

    /**
     * 获得预警监控项配置表（new预警）列表
     *
     * @param ids 编号
     * @return 预警监控项配置表（new预警）列表
     */
    List<WarningMonitorDO> getWarningMonitorList(Collection<String> ids);

    /**
     * 获得预警监控项配置表（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警监控项配置表（new预警）分页
     */
    PageResult<WarningMonitorDO> getWarningMonitorPage(WarningMonitorPageReqVO pageReqVO);

    /**
     * 获得预警监控项配置表（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警监控项配置表（new预警）列表
     */
    List<WarningMonitorDO> getWarningMonitorList(WarningMonitorExportReqVO exportReqVO);

    List<WarningMonitorRespVO> list(WarningMonitorListReqVO ids);
}
