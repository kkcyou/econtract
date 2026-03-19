package com.yaoan.module.econtract.service.workbenchmanage;

import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchCreateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchExportReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchPageReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchUpdateReqVO;
import com.yaoan.module.econtract.convert.workbenchmanage.WorkbenchConvert;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;
import com.yaoan.module.econtract.dal.mysql.workbenchmanage.WorkbenchMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 工作台管理 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class WorkbenchServiceImpl implements WorkbenchService {

    @Resource
    private WorkbenchMapper workbenchMapper;

    @Override
    public String createWorkbench(WorkbenchCreateReqVO createReqVO) {
        // 插入
        WorkbenchDO workbench = WorkbenchConvert.INSTANCE.convert(createReqVO);
        workbenchMapper.insert(workbench);
        // 返回
        return workbench.getId();
    }

    @Override
    public void updateWorkbench(WorkbenchUpdateReqVO updateReqVO) {
        // 校验存在
        validateWorkbenchExists(updateReqVO.getId());
        // 更新
        WorkbenchDO updateObj = WorkbenchConvert.INSTANCE.convert(updateReqVO);
        workbenchMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkbench(String id) {
        // 校验存在
        validateWorkbenchExists(id);
        // 删除
        workbenchMapper.deleteById(id);
    }

    private void validateWorkbenchExists(String id) {
        if (workbenchMapper.selectById(id) == null) {
            throw exception(500,"查询不到数据");
        }
    }

    @Override
    public WorkbenchDO getWorkbench(String id) {
        return workbenchMapper.selectById(id);
    }

    @Override
    public List<WorkbenchDO> getWorkbenchList(Collection<String> ids) {
        return workbenchMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WorkbenchDO> getWorkbenchPage(WorkbenchPageReqVO pageReqVO) {
        return workbenchMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WorkbenchDO> getWorkbenchList(WorkbenchExportReqVO exportReqVO) {
        return workbenchMapper.selectList(exportReqVO);
    }

}
