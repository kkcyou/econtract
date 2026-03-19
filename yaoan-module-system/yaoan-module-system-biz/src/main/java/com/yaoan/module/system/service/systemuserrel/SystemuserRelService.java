package com.yaoan.module.system.service.systemuserrel;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelCreateReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelExportReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelPageReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelUpdateReqVO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


/**
 * 系统对接用户关系 Service 接口
 *
 * @author lls
 */
public interface SystemuserRelService {

    /**
     * 创建系统对接用户关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createSystemuserRel(@Valid SystemuserRelCreateReqVO createReqVO);

    /**
     * 更新系统对接用户关系
     *
     * @param updateReqVO 更新信息
     */
    void updateSystemuserRel(@Valid SystemuserRelUpdateReqVO updateReqVO);

    /**
     * 删除系统对接用户关系
     *
     * @param id 编号
     */
    void deleteSystemuserRel(String id);

    /**
     * 获得系统对接用户关系
     *
     * @param id 编号
     * @return 系统对接用户关系
     */
    SystemuserRelDO getSystemuserRel(String id);

    /**
     * 获得系统对接用户关系列表
     *
     * @param ids 编号
     * @return 系统对接用户关系列表
     */
    List<SystemuserRelDO> getSystemuserRelList(Collection<String> ids);

    /**
     * 获得系统对接用户关系分页
     *
     * @param pageReqVO 分页查询
     * @return 系统对接用户关系分页
     */
    PageResult<SystemuserRelDO> getSystemuserRelPage(SystemuserRelPageReqVO pageReqVO);

    /**
     * 获得系统对接用户关系列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 系统对接用户关系列表
     */
    List<SystemuserRelDO> getSystemuserRelList(SystemuserRelExportReqVO exportReqVO);

}
