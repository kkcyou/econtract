package com.yaoan.module.econtract.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description:
 * @author: Hong
 * @date: 2023/9/18 15:16
 */
@Slf4j
@Component("contractEffectOrOverdueJob")
public class ContractEffectOrOverdueJob implements JobHandler {
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private ContractPerforMapper performanceMapper;

    @Override
    @Transactional
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.debug("开始执行合同生效或过期任务～");
        //1.查询合同开始生效时间为昨天，履约状态为待履约的履约数据--获取刚刚生效的数据
        List<ContractPerformanceDO> contractPerformanceDOS = performanceMapper.selectList(new LambdaQueryWrapperX<ContractPerformanceDO>()
                .eqIfPresent(ContractPerformanceDO::getContractStatus, ContractPerfEnums.WAIT_PERFORMANCE.getCode())
                .eqIfPresent(ContractPerformanceDO::getValidity0,DateUtil.beginOfDay(DateUtil.yesterday())));
        if (CollectionUtil.isNotEmpty(contractPerformanceDOS)) {
            //时间生效后修改履约状态为待履约的改为履约中
            contractPerformanceDOS.forEach(item -> item.setContractStatus(ContractPerfEnums.IN_PERFORMANCE.getCode()));
            performanceMapper.updateBatch(contractPerformanceDOS);
           //修改履约任务 为履约中  待履约
            for (ContractPerformanceDO contractPerformanceDO : contractPerformanceDOS) {
                List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>().eqIfPresent(PerfTaskDO::getContractPerfId, contractPerformanceDO.getId()));
                if(CollectionUtil.isNotEmpty(perfTaskDOS)){
                    setPerfStatus(perfTaskDOS,contractPerformanceDO.getId());
                    perforTaskMapper.updateBatch(perfTaskDOS);
                }
            }
        }
        //2.合同下无履约任务，当合同结束日期已到时，合同状态自动变为“履约结束”
        //查询待建立履约,且结束日期为昨天的信息--获取刚刚失效数据
        List<ContractPerformanceDO> contractPerformanceDOS1 = performanceMapper.selectList(new LambdaQueryWrapperX<ContractPerformanceDO>()
                .eqIfPresent(ContractPerformanceDO::getContractStatus, ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode())
                .eqIfPresent(ContractPerformanceDO::getValidity1,DateUtil.beginOfDay(DateUtil.yesterday())));
        if(CollectionUtil.isNotEmpty(contractPerformanceDOS1)){
            //合同状态自动变为“履约结束”
            contractPerformanceDOS1.forEach(item -> item.setContractStatus(ContractPerfEnums.PERFORMANCE_END.getCode()));
            performanceMapper.updateBatch(contractPerformanceDOS1);
        }
        return "合同生效或过期处理完成";
    }
    private void setPerfStatus(List<PerfTaskDO> perfTaskDOS,String contractPperfId) {
        //2.获取同一合同履约下任务状态为未开始的最早的一个履约任务
        PerfTaskDO perfTaskDO = perforTaskMapper.queryLastPerfTask(contractPperfId, PerfTaskEnums.PERFTASK_NO_START.getCode(), "asc",null);
       for (PerfTaskDO entry:perfTaskDOS) {
           if(perfTaskDO.getId().equals(entry.getId())){
               entry.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
           }else {
               entry.setTaskStatus(PerfTaskEnums.WAIT_PERFORMANCE.getCode());
           }
        }

    }
}
