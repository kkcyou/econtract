package com.yaoan.module.econtract.service.warningrulemonitorrel;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningrulemonitor.WarningRuleMonitorConvert;
import com.yaoan.module.econtract.dal.mysql.warningrulemonitorrel.WarningRuleMonitorRelMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 预警规则与监控项关联关系表（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningRuleMonitorRelServiceImpl implements WarningRuleMonitorRelService {

    @Resource
    private WarningRuleMonitorRelMapper warningRuleMonitorMapper;

    @Override
    public String createWarningRuleMonitor(WarningRuleMonitorCreateReqVO createReqVO) {
        // 插入
        WarningRuleMonitorRelDO warningRuleMonitor = WarningRuleMonitorConvert.INSTANCE.convert(createReqVO);
        warningRuleMonitorMapper.insert(warningRuleMonitor);
        // 返回
        return warningRuleMonitor.getId();
    }

    @Override
    public void updateWarningRuleMonitor(WarningRuleMonitorUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningRuleMonitorExists(updateReqVO.getId());
        // 更新
        WarningRuleMonitorRelDO updateObj = WarningRuleMonitorConvert.INSTANCE.convert(updateReqVO);
        warningRuleMonitorMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningRuleMonitor(String id) {
        // 校验存在
        validateWarningRuleMonitorExists(id);
        // 删除
        warningRuleMonitorMapper.deleteById(id);
    }

    private void validateWarningRuleMonitorExists(String id) {
        if (warningRuleMonitorMapper.selectById(id) == null) {
//            throw exception(WARNING_RULE_MONITOR_NOT_EXISTS);
        }
    }

    @Override
    public WarningRuleMonitorRelDO getWarningRuleMonitor(String id) {
        return warningRuleMonitorMapper.selectById(id);
    }

    @Override
    public List<WarningRuleMonitorRelDO> getWarningRuleMonitorList(Collection<String> ids) {
        return warningRuleMonitorMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningRuleMonitorRelDO> getWarningRuleMonitorPage(WarningRuleMonitorPageReqVO pageReqVO) {
        return warningRuleMonitorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningRuleMonitorRelDO> getWarningRuleMonitorList(WarningRuleMonitorExportReqVO exportReqVO) {
        return warningRuleMonitorMapper.selectList(exportReqVO);
    }

}
