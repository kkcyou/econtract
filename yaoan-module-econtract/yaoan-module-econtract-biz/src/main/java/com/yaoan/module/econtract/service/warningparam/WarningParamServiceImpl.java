package com.yaoan.module.econtract.service.warningparam;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningparam.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningparam.WarningParamConvert;
import com.yaoan.module.econtract.dal.mysql.warningparam.WarningParamMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 预警消息模板参数(new预警) Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningParamServiceImpl implements WarningParamService {

    @Resource
    private WarningParamMapper warningParamMapper;

    @Override
    public String createWarningParam(WarningParamCreateReqVO createReqVO) {
        // 插入
        WarningParamDO warningParam = WarningParamConvert.INSTANCE.convert(createReqVO);
        warningParamMapper.insert(warningParam);
        // 返回
        return warningParam.getId();
    }

    @Override
    public void updateWarningParam(WarningParamUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningParamExists(updateReqVO.getId());
        // 更新
        WarningParamDO updateObj = WarningParamConvert.INSTANCE.convert(updateReqVO);
        warningParamMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningParam(String id) {
        // 校验存在
        validateWarningParamExists(id);
        // 删除
        warningParamMapper.deleteById(id);
    }

    private void validateWarningParamExists(String id) {
        if (warningParamMapper.selectById(id) == null) {
//            throw exception(WARNING_PARAM_NOT_EXISTS);
        }
    }

    @Override
    public WarningParamDO getWarningParam(String id) {
        return warningParamMapper.selectById(id);
    }

    @Override
    public List<WarningParamDO> getWarningParamList(Collection<String> ids) {
        return warningParamMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningParamDO> getWarningParamPage(WarningParamPageReqVO pageReqVO) {
        return warningParamMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningParamDO> getWarningParamList(WarningParamExportReqVO exportReqVO) {
        return warningParamMapper.selectList(exportReqVO);
    }

}
