package com.yaoan.module.system.api.dept;

import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.convert.dept.DeptConvert;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.service.dept.DeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 部门 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class DeptApiImpl implements DeptApi {

    @Resource
    private DeptService deptService;

    @Override
    @DataPermission(enable = false)
    public DeptRespDTO getDept(Long id) {
        DeptDO dept = deptService.getDept(id);
        return DeptConvert.INSTANCE.convert03(dept);
    }

    @Override
    @DataPermission(enable = false)
    public DeptRespDTO getDeptByParentId(Long parentId) {
        DeptDO dept = deptService.getDeptByParentId(parentId);
        return DeptConvert.INSTANCE.convert03(dept);
    }

    @Override
    public List<DeptRespDTO> getDeptList(Collection<Long> ids) {
        Map<String, List<DeptDO>> map = new HashMap();
        DataPermissionUtils.executeIgnore(() -> {
            map.put("depts", deptService.getDeptList(ids));
        });
        return DeptConvert.INSTANCE.convertList03(map.get("depts"));
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        deptService.validateDeptList(ids);
    }

    @Override
    @DataPermission(enable = false)
    public List<DeptRespDTO> getDeptList() {
        List<DeptDO> depts = deptService.getDeptList();
        return DeptConvert.INSTANCE.convertList03(depts);
    }

    @Override
    public List<DeptRespDTO> getDeptListByCompanyId(Long companyId) {
        List<DeptDO> depts = deptService.getDeptListByCompanyId(companyId);
        return DeptConvert.INSTANCE.convertList03(depts);
    }

}
