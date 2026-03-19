package com.yaoan.module.econtract.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author doujiale
 */
@Slf4j
@Component
public class ContractPerformanceOverdueJob implements JobHandler {

    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private ContractPerforMapper performanceMapper;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {

        log.debug("执行了合同履约过期判断任务～");
        //1.查询履约时间为昨天，履约状态为履约中和履约暂停的履约任务数据
        List<PerfTaskDO> perfTaskDOList = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>()
                .inIfPresent(PerfTaskDO::getTaskStatus, CollectionUtil.newArrayList(PerfTaskEnums.IN_PERFORMANCE.getCode(), PerfTaskEnums.PERFORMANCE_PAUSE.getCode()))
                .eqIfPresent(PerfTaskDO::getPerfTime, DateUtil.beginOfDay(DateUtil.yesterday())));

        if (CollectionUtil.isNotEmpty(perfTaskDOList)) {
            Map<Integer, List<PerfTaskDO>> statusMap = perfTaskDOList.stream().collect(Collectors.groupingBy(PerfTaskDO::getTaskStatus));
            //修改履约中的履约任务状态为履约逾期  同时修改任务表和履约表
            List<PerfTaskDO> performanceInTasks = statusMap.get(PerfTaskEnums.IN_PERFORMANCE.getCode());
            if (CollectionUtil.isNotEmpty(performanceInTasks)) {
                performanceInTasks.forEach(item -> item.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode()));
                perforTaskMapper.updateBatch(performanceInTasks);
                Set<String> performanceIds = performanceInTasks.stream().map(PerfTaskDO::getContractPerfId).collect(Collectors.toSet());
                List<ContractPerformanceDO> contractPerformances = performanceMapper.selectBatchIds(performanceIds);
                contractPerformances.forEach(item -> item.setContractStatus(ContractPerfEnums.PERFORMANCE_OVER_TIME.getCode()));
                performanceMapper.updateBatch(contractPerformances);
            }

            //修改履约暂停的履约任务状态为超期暂停  同时修改任务表和履约表
            List<PerfTaskDO> performancePauseTasks = statusMap.get(PerfTaskEnums.PERFORMANCE_PAUSE.getCode());
            if (CollectionUtil.isNotEmpty(performancePauseTasks)) {
                performancePauseTasks.forEach(item -> item.setTaskStatus(PerfTaskEnums.OVER_TIME_PAUSE.getCode()));
                perforTaskMapper.updateBatch(performancePauseTasks);
                Set<String> performanceIds = performancePauseTasks.stream().map(PerfTaskDO::getContractPerfId).collect(Collectors.toSet());
                List<ContractPerformanceDO> contractPerformances = performanceMapper.selectBatchIds(performanceIds);
                contractPerformances.forEach(item -> item.setContractStatus(ContractPerfEnums.OVER_TIME_PAUSE.getCode()));
                performanceMapper.updateBatch(contractPerformances);
            }

        }

        return "执行合同履约过期任务完成";
    }

}
