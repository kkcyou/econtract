package com.yaoan.module.econtract.service.warningcfg;

import java.util.*;
import javax.validation.*;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.*;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningPageRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.update.WarningCfgTurnReqVO;
import com.yaoan.module.econtract.dal.dataobject.warningcfg.WarningCfgDO;

/**
 * 预警检查配置表(new预警) Service 接口
 *
 * @author admin
 */
public interface WarningCfgService {

    /**
     * 创建预警检查配置表(new预警)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningCfg(@Valid WarningCfgCreateReqVO createReqVO);

    /**
     * 更新预警检查配置表(new预警)
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningCfg(@Valid WarningCfgUpdateReqVO updateReqVO);

    /**
     * 删除预警检查配置表(new预警)
     *
     * @param id 编号
     */
    void deleteWarningCfg(String id);

    /**
     * 获得预警检查配置表(new预警)
     *
     * @param id 编号
     * @return 预警检查配置表(new预警)
     */
    WarningCfgRespVO getWarningCfg(String id);

    /**
     * 获得预警检查配置表(new预警)列表
     *
     * @param ids 编号
     * @return 预警检查配置表(new预警)列表
     */
    List<WarningCfgDO> getWarningCfgList(Collection<String> ids);

    /**
     * 获得预警检查配置表(new预警)分页
     *
     * @param pageReqVO 分页查询
     * @return 预警检查配置表(new预警)分页
     */
    PageResult<WarningPageRespVO> getWarningCfgPage(WarningCfgPageReqVO pageReqVO);

    /**
     * 获得预警检查配置表(new预警)列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警检查配置表(new预警)列表
     */
    List<WarningCfgDO> getWarningCfgList(WarningCfgExportReqVO exportReqVO);


    /**
     * 预警检查  flag : 1 手动 2 定时, modelName : 模块来源code
     */
    void warningCheck(String flag, TaskForWarningReqDTO params, String businessId, String... modelCode);

    void updateStatus(WarningCfgTurnReqVO reqVO);
}
