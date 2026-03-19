package com.yaoan.module.econtract.service.warningmodel;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningmodel.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningmodel.WarningModelConvert;
import com.yaoan.module.econtract.dal.mysql.warningmodel.WarningModelMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 预警模块来源（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningModelServiceImpl implements WarningModelService {

    @Resource
    private WarningModelMapper warningModelMapper;

    @Override
    public String createWarningModel(WarningModelCreateReqVO createReqVO) {
        // 插入
        WarningModelDO warningModel = WarningModelConvert.INSTANCE.convert(createReqVO);
        warningModelMapper.insert(warningModel);
        // 返回
        return warningModel.getId();
    }

    @Override
    public void updateWarningModel(WarningModelUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningModelExists(updateReqVO.getId());
        // 更新
        WarningModelDO updateObj = WarningModelConvert.INSTANCE.convert(updateReqVO);
        warningModelMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningModel(String id) {
        // 校验存在
        validateWarningModelExists(id);
        // 删除
        warningModelMapper.deleteById(id);
    }

    private void validateWarningModelExists(String id) {
        if (warningModelMapper.selectById(id) == null) {
//            throw exception(WARNING_MODEL_NOT_EXISTS);
        }
    }

    @Override
    public WarningModelDO getWarningModel(String id) {
        return warningModelMapper.selectById(id);
    }

    @Override
    public List<WarningModelDO> getWarningModelList( ) {
        return warningModelMapper.selectList();
    }

    @Override
    public PageResult<WarningModelDO> getWarningModelPage(WarningModelPageReqVO pageReqVO) {
        return warningModelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningModelDO> getWarningModelList(WarningModelExportReqVO exportReqVO) {
        return warningModelMapper.selectList(exportReqVO);
    }

}
