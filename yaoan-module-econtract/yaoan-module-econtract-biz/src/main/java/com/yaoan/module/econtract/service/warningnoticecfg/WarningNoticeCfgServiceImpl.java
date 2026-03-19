package com.yaoan.module.econtract.service.warningnoticecfg;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningnoticecfg.WarningNoticeCfgConvert;
import com.yaoan.module.econtract.dal.mysql.warningnoticecfg.WarningNoticeCfgMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 预警通知配置表（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningNoticeCfgServiceImpl implements WarningNoticeCfgService {

    @Resource
    private WarningNoticeCfgMapper warningNoticeCfgMapper;

    @Override
    public String createWarningNoticeCfg(WarningNoticeCfgCreateReqVO createReqVO) {
        // 插入
        WarningNoticeCfgDO warningNoticeCfg = WarningNoticeCfgConvert.INSTANCE.convert(createReqVO);
        warningNoticeCfgMapper.insert(warningNoticeCfg);
        // 返回
        return warningNoticeCfg.getId();
    }

    @Override
    public void updateWarningNoticeCfg(WarningNoticeCfgUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningNoticeCfgExists(updateReqVO.getId());
        // 更新
        WarningNoticeCfgDO updateObj = WarningNoticeCfgConvert.INSTANCE.convert(updateReqVO);
        warningNoticeCfgMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningNoticeCfg(String id) {
        // 校验存在
        validateWarningNoticeCfgExists(id);
        // 删除
        warningNoticeCfgMapper.deleteById(id);
    }

    private void validateWarningNoticeCfgExists(String id) {
        if (warningNoticeCfgMapper.selectById(id) == null) {
//            throw exception(WARNING_NOTICE_CFG_NOT_EXISTS);
        }
    }

    @Override
    public WarningNoticeCfgDO getWarningNoticeCfg(String id) {
        return warningNoticeCfgMapper.selectById(id);
    }

    @Override
    public List<WarningNoticeCfgDO> getWarningNoticeCfgList(Collection<String> ids) {
        return warningNoticeCfgMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningNoticeCfgDO> getWarningNoticeCfgPage(WarningNoticeCfgPageReqVO pageReqVO) {
        return warningNoticeCfgMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningNoticeCfgDO> getWarningNoticeCfgList(WarningNoticeCfgExportReqVO exportReqVO) {
        return warningNoticeCfgMapper.selectList(exportReqVO);
    }

}
