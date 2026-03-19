package com.yaoan.module.system.service.systemuserrel;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelCreateReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelExportReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelPageReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelUpdateReqVO;
import com.yaoan.module.system.convert.systemuserrel.SystemuserRelConvert;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 系统对接用户关系 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class SystemuserRelServiceImpl implements SystemuserRelService {

    @Resource
    private SystemuserRelMapper systemuserRelMapper;

    @Override
    public String createSystemuserRel(SystemuserRelCreateReqVO createReqVO) {
        // 插入
        SystemuserRelDO systemuserRel = SystemuserRelConvert.INSTANCE.convert(createReqVO);
        systemuserRelMapper.insert(systemuserRel);
        // 返回
        return systemuserRel.getId();
    }

    @Override
    public void updateSystemuserRel(SystemuserRelUpdateReqVO updateReqVO) {
        // 校验存在
        validateSystemuserRelExists(updateReqVO.getId());
        // 更新
        SystemuserRelDO updateObj = SystemuserRelConvert.INSTANCE.convert(updateReqVO);
        systemuserRelMapper.updateById(updateObj);
    }

    @Override
    public void deleteSystemuserRel(String id) {
        // 校验存在
        validateSystemuserRelExists(id);
        // 删除
        systemuserRelMapper.deleteById(id);
    }

    private void validateSystemuserRelExists(String id) {
        if (systemuserRelMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"数据不存在");
        }
    }

    @Override
    public SystemuserRelDO getSystemuserRel(String id) {
        return systemuserRelMapper.selectById(id);
    }

    @Override
    public List<SystemuserRelDO> getSystemuserRelList(Collection<String> ids) {
        return systemuserRelMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SystemuserRelDO> getSystemuserRelPage(SystemuserRelPageReqVO pageReqVO) {
        return systemuserRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SystemuserRelDO> getSystemuserRelList(SystemuserRelExportReqVO exportReqVO) {
        return systemuserRelMapper.selectList(exportReqVO);
    }

}
