package com.yaoan.module.econtract.service.roleworkbenchrel;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelExportReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelPageReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchRespVO;
import com.yaoan.module.econtract.convert.roleworkbenchrel.RoleWorkbenchRelConvert;
import com.yaoan.module.econtract.convert.workbenchmanage.WorkbenchConvert;
import com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel.RoleWorkbenchRelDO;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;
import com.yaoan.module.econtract.dal.mysql.roleworkbenchrel.RoleWorkbenchRelMapper;
import com.yaoan.module.econtract.service.workbenchmanage.WorkbenchService;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;


import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 角色工作台关联 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class RoleWorkbenchRelServiceImpl implements RoleWorkbenchRelService {

    @Resource
    private RoleWorkbenchRelMapper roleWorkbenchRelMapper;
    @Resource
    private RoleApi roleApi;
    @Resource
    private WorkbenchService workbenchService;
    @Override
    public String createRoleWorkbenchRel(RoleWorkbenchRelCreateReqVO createReqVO) {
        Long roleId = createReqVO.getRoleId();
        String workbenchId = createReqVO.getWorkbenchId();
        if(roleId == null || StringUtils.isEmpty(workbenchId)){
            throw exception(500,"请传入正确的参数");
        }
        List<RoleWorkbenchRelDO> roleWorkbenchRelDOList = roleWorkbenchRelMapper.selectList(RoleWorkbenchRelDO::getRoleId, roleId);
        if(roleWorkbenchRelDOList.size() > 0){
            RoleWorkbenchRelDO roleWorkbenchRel =  roleWorkbenchRelDOList.get(0).setWorkbenchId(workbenchId);
            roleWorkbenchRelMapper.updateById(roleWorkbenchRel);
            return roleWorkbenchRel.getId();
        }else{
            // 插入
            RoleWorkbenchRelDO roleWorkbenchRel = new RoleWorkbenchRelDO();//RoleWorkbenchRelConvert.INSTANCE.convert(createReqVO);
            roleWorkbenchRel.setWorkbenchId(workbenchId);
            roleWorkbenchRel.setRoleId(roleId);
            roleWorkbenchRelMapper.insert(roleWorkbenchRel);
            // 返回
            return roleWorkbenchRel.getId();
        }
       
       
    }

    @Override
    public void updateRoleWorkbenchRel(RoleWorkbenchRelUpdateReqVO updateReqVO) {
        // 校验存在
        validateRoleWorkbenchRelExists(updateReqVO.getId());
        // 更新
        RoleWorkbenchRelDO updateObj = RoleWorkbenchRelConvert.INSTANCE.convert(updateReqVO);
        roleWorkbenchRelMapper.updateById(updateObj);
    }

    @Override
    public void deleteRoleWorkbenchRel(String id) {
        // 校验存在
        validateRoleWorkbenchRelExists(id);
        // 删除
        roleWorkbenchRelMapper.deleteById(id);
    }

    private void validateRoleWorkbenchRelExists(String id) {
        if (roleWorkbenchRelMapper.selectById(id) == null) {
            throw exception(500,"查询不到数据");
        }
    }

    @Override
    public RoleWorkbenchRelDO getRoleWorkbenchRel(String id) {
        return roleWorkbenchRelMapper.selectById(id);
    }

    @Override
    public List<RoleWorkbenchRelDO> getRoleWorkbenchRelList(Collection<String> ids) {
        return roleWorkbenchRelMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<RoleWorkbenchRelDO> getRoleWorkbenchRelPage(RoleWorkbenchRelPageReqVO pageReqVO) {
        return roleWorkbenchRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RoleWorkbenchRelDO> getRoleWorkbenchRelList(RoleWorkbenchRelExportReqVO exportReqVO) {
        return roleWorkbenchRelMapper.selectList(exportReqVO);
    }
    public WorkbenchRespVO getWorkBenchInfo(RoleWorkbenchRelCreateReqVO getReq){
        WorkbenchRespVO workBenchInfo = new WorkbenchRespVO();
      
        Long roleId = getReq.getRoleId();
        if(roleId == null){
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            RoleRespDTO roleRespDTO = roleApi.getRole(getLoginUserId());
            if(roleRespDTO == null){
                return null;
            }
            roleId = roleRespDTO.getId();
        }
        List<RoleWorkbenchRelDO> roleWorkbenchRelDOList =  roleWorkbenchRelMapper.selectList(RoleWorkbenchRelDO::getRoleId, roleId);
        if(roleWorkbenchRelDOList.size()>0){
            WorkbenchDO workbenchDO = workbenchService.getWorkbench(roleWorkbenchRelDOList.get(0).getWorkbenchId());
            WorkbenchRespVO workbenchRespVO = WorkbenchConvert.INSTANCE.convert(workbenchDO);
            return workbenchRespVO;
        }
        return workBenchInfo;
    }
}
