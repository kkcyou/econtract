package com.yaoan.module.econtract.service.workbenchmanage;

import java.util.*;
import javax.validation.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchCreateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchExportReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchPageReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;

/**
 * 工作台管理 Service 接口
 *
 * @author lls
 */
public interface WorkbenchService {

    /**
     * 创建工作台管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWorkbench(@Valid WorkbenchCreateReqVO createReqVO);

    /**
     * 更新工作台管理
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkbench(@Valid WorkbenchUpdateReqVO updateReqVO);

    /**
     * 删除工作台管理
     *
     * @param id 编号
     */
    void deleteWorkbench(String id);

    /**
     * 获得工作台管理
     *
     * @param id 编号
     * @return 工作台管理
     */
    WorkbenchDO getWorkbench(String id);

    /**
     * 获得工作台管理列表
     *
     * @param ids 编号
     * @return 工作台管理列表
     */
    List<WorkbenchDO> getWorkbenchList(Collection<String> ids);

    /**
     * 获得工作台管理分页
     *
     * @param pageReqVO 分页查询
     * @return 工作台管理分页
     */
    PageResult<WorkbenchDO> getWorkbenchPage(WorkbenchPageReqVO pageReqVO);

    /**
     * 获得工作台管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 工作台管理列表
     */
    List<WorkbenchDO> getWorkbenchList(WorkbenchExportReqVO exportReqVO);

}
