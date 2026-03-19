package com.yaoan.module.econtract.service.warningmonitor;

import cn.hutool.core.collection.CollUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningmonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningmonitor.WarningMonitorConvert;
import com.yaoan.module.econtract.dal.mysql.warningmonitor.WarningMonitorMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 预警监控项配置表（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningMonitorServiceImpl implements WarningMonitorService {

    @Resource
    private WarningMonitorMapper warningMonitorMapper;

    @Override
    public String createWarningMonitor(WarningMonitorCreateReqVO createReqVO) {
        // 插入
        WarningMonitorDO warningMonitor = WarningMonitorConvert.INSTANCE.convert(createReqVO);
        warningMonitorMapper.insert(warningMonitor);
        // 返回
        return warningMonitor.getId();
    }

    @Override
    public void updateWarningMonitor(WarningMonitorUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningMonitorExists(updateReqVO.getId());
        // 更新
        WarningMonitorDO updateObj = WarningMonitorConvert.INSTANCE.convert(updateReqVO);
        warningMonitorMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningMonitor(String id) {
        // 校验存在
        validateWarningMonitorExists(id);
        // 删除
        warningMonitorMapper.deleteById(id);
    }

    private void validateWarningMonitorExists(String id) {
        if (warningMonitorMapper.selectById(id) == null) {
//            throw exception(WARNING_MONITOR_NOT_EXISTS);
        }
    }

    @Override
    public WarningMonitorDO getWarningMonitor(String id) {
        return warningMonitorMapper.selectById(id);
    }

    @Override
    public List<WarningMonitorDO> getWarningMonitorList(Collection<String> ids) {
        return warningMonitorMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningMonitorDO> getWarningMonitorPage(WarningMonitorPageReqVO pageReqVO) {
        return warningMonitorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningMonitorDO> getWarningMonitorList(WarningMonitorExportReqVO exportReqVO) {
        return warningMonitorMapper.selectList(exportReqVO);
    }

    @Override
    public List<WarningMonitorRespVO> list(WarningMonitorListReqVO idReqVO) {
        List<WarningMonitorDO> monitorDOList = warningMonitorMapper.selectList(new LambdaQueryWrapperX<WarningMonitorDO>().inIfPresent(WarningMonitorDO::getModelId,idReqVO.getIdList()).eqIfPresent(WarningMonitorDO::getModelId, idReqVO.getModelId()).isNull(WarningMonitorDO::getParentId));
        if(CollUtil.isEmpty(monitorDOList)){
            return Collections.emptyList();
        }
        List<WarningMonitorRespVO> respVOList = WarningMonitorConvert.INSTANCE.listDo2Resp(monitorDOList);
        return respVOList;
    }

}
