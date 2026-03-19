package com.yaoan.module.econtract.api.bpm.performance;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.mysql.bpm.performance.suspend.BpmPerformanceMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ContractApi 实现类
 *
 * @author doujl
 */
@Service
public class PerformanceApiImpl implements PerformanceApi {

    @Resource
    private ContractPerforMapper performanceMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private BpmPerformanceMapper bpmPerformanceMapper;

    @Override
    @DataPermission(enable = false)
    public void notifyPerformanceApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {

        BpmPerformance bpmPerformance = bpmPerformanceMapper.selectById(businessKey);
        if (bpmPerformance != null) {
            //1.更新申请表状态
            bpmPerformance.setResult(statusEnums.getResult());
            bpmPerformanceMapper.updateById(bpmPerformance);
            //2.更新合同履约表状态
            ContractPerformanceDO performanceDO = performanceMapper.selectById(bpmPerformance.getPerformanceId());
            if (performanceDO != null) {
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    performanceDO.setContractStatus(ContractPerfEnums.CONTRACT_PAUSE.getCode());
                    List<PerfTaskDO> tasks = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getContractPerfId, performanceDO.getId())
                            .eq(PerfTaskDO::getTaskStatus, PerfTaskEnums.IN_PERFORMANCE.getCode())
                            .or()
                            .eq(PerfTaskDO::getTaskStatus, PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode()));
                    if (CollectionUtil.isNotEmpty(tasks)) {

                        List<PerfTaskDO> inPerformanceTasks = tasks.stream().filter(task -> task.getTaskStatus().equals(PerfTaskEnums.IN_PERFORMANCE.getCode())).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(inPerformanceTasks)) {
                            inPerformanceTasks.forEach(item -> item.setTaskStatus(PerfTaskEnums.PERFORMANCE_PAUSE.getCode()));
                            perforTaskMapper.updateBatch(inPerformanceTasks);
                        }
                        List<PerfTaskDO> overTimeTasks = tasks.stream().filter(task -> task.getTaskStatus().equals(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode())).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(overTimeTasks)) {
                            overTimeTasks.forEach(item -> item.setTaskStatus(PerfTaskEnums.OVER_TIME_PAUSE.getCode()));
                            perforTaskMapper.updateBatch(overTimeTasks);
                        }
                    }
                }
                if (BpmProcessInstanceResultEnum.REJECT == statusEnums) {
                    performanceDO.setContractStatus(ContractPerfEnums.IN_PERFORMANCE.getCode());
                    if (performanceDO.getPerfTime() != null && DateUtils.isExpired(performanceDO.getPerfTime())) {
                        performanceDO.setContractStatus(ContractPerfEnums.PERFORMANCE_OVER_TIME.getCode());
                    }
                }
                performanceMapper.updateById(performanceDO);
            }
        }
    }
}
