package com.yaoan.module.econtract.service.roleworkbenchrel;

import java.util.*;
import javax.validation.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelExportReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelPageReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchRespVO;
import com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel.RoleWorkbenchRelDO;

/**
 * 角色工作台关联 Service 接口
 *
 * @author admin
 */
public interface RoleWorkbenchRelService {

    /**
     * 创建角色工作台关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRoleWorkbenchRel(@Valid RoleWorkbenchRelCreateReqVO createReqVO);

    /**
     * 更新角色工作台关联
     *
     * @param updateReqVO 更新信息
     */
    void updateRoleWorkbenchRel(@Valid RoleWorkbenchRelUpdateReqVO updateReqVO);

    /**
     * 删除角色工作台关联
     *
     * @param id 编号
     */
    void deleteRoleWorkbenchRel(String id);

    /**
     * 获得角色工作台关联
     *
     * @param id 编号
     * @return 角色工作台关联
     */
    RoleWorkbenchRelDO getRoleWorkbenchRel(String id);

    /**
     * 获得角色工作台关联列表
     *
     * @param ids 编号
     * @return 角色工作台关联列表
     */
    List<RoleWorkbenchRelDO> getRoleWorkbenchRelList(Collection<String> ids);

    /**
     * 获得角色工作台关联分页
     *
     * @param pageReqVO 分页查询
     * @return 角色工作台关联分页
     */
    PageResult<RoleWorkbenchRelDO> getRoleWorkbenchRelPage(RoleWorkbenchRelPageReqVO pageReqVO);

    /**
     * 获得角色工作台关联列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 角色工作台关联列表
     */
    List<RoleWorkbenchRelDO> getRoleWorkbenchRelList(RoleWorkbenchRelExportReqVO exportReqVO);

    /**
     * 查询工作台信息
     * @return
     */
    public WorkbenchRespVO getWorkBenchInfo(RoleWorkbenchRelCreateReqVO getReq);
}
