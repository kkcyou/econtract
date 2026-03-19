package com.yaoan.module.econtract.service.warningmodel;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningmodel.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警模块来源（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningModelService {

    /**
     * 创建预警模块来源（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningModel(@Valid WarningModelCreateReqVO createReqVO);

    /**
     * 更新预警模块来源（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningModel(@Valid WarningModelUpdateReqVO updateReqVO);

    /**
     * 删除预警模块来源（new预警）
     *
     * @param id 编号
     */
    void deleteWarningModel(String id);

    /**
     * 获得预警模块来源（new预警）
     *
     * @param id 编号
     * @return 预警模块来源（new预警）
     */
    WarningModelDO getWarningModel(String id);

    /**
     * 获得预警模块来源（new预警）列表
     *
     * @return 预警模块来源（new预警）列表
     */
    List<WarningModelDO> getWarningModelList( );

    /**
     * 获得预警模块来源（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警模块来源（new预警）分页
     */
    PageResult<WarningModelDO> getWarningModelPage(WarningModelPageReqVO pageReqVO);

    /**
     * 获得预警模块来源（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警模块来源（new预警）列表
     */
    List<WarningModelDO> getWarningModelList(WarningModelExportReqVO exportReqVO);

}
