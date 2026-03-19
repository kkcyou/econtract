package com.yaoan.module.econtract.job.riskalert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.riskalert.RiskAlertDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.riskalert.RiskAlertMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yaoan.module.econtract.enums.PerfTaskEnums.PERFORMANCE_OVER_TIME;
import static com.yaoan.module.econtract.enums.RiskAlertEnums.ALERT_SOURCE_PERFORMANCE;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/18 23:02
 */
@Slf4j
@Component("riskAlertCheckOverduePerformTaskJob")
public class RiskAlertCheckOverduePerformTaskJob implements JobHandler {

    @Resource
    private PerforTaskMapper taskMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private RiskAlertMapper riskAlertMapper;
    @Resource
    private ContractPerforMapper contractPerforMapper;

    @Override
    @TenantIgnore
    @DataPermission(enable = false)
    public String execute(String param) throws Exception {
        log.info("开始执行风险检测任务。");
        log.info("start to execute check overdue perform tasks job...");
        Date yesterday = DateUtil.yesterday();
        Date beginTime = DateUtil.beginOfDay(yesterday);
        Date endTime = DateUtil.endOfDay(yesterday);
        //找出前一天的状态为履约超期的任务
        List<PerfTaskDO> taskDOS = taskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>()
                .between(PerfTaskDO::getPerfTime, beginTime, endTime)
                .eq(PerfTaskDO::getTaskStatus, PERFORMANCE_OVER_TIME.getCode()));

        List<PerfTaskDO> perfTaskDOS = taskMapper.selectList();

        if (CollectionUtil.isEmpty(taskDOS)) {
            return null;
        }

        if (CollectionUtil.isNotEmpty(taskDOS)) {
            Map<String, PerfTaskDO> perfTaskDOMap = CollectionUtils.convertMap(perfTaskDOS, PerfTaskDO::getId);
            List<RiskAlertDO> riskAlertDOS = new ArrayList<RiskAlertDO>();
            List<ContractDO> contractDOS = contractMapper.selectList();
            Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);

            //查履约 ，履约表有合同名称

            List<ContractPerformanceDO> contractPerformanceDOS = contractPerforMapper.selectList();
            Map<String, ContractPerformanceDO> contractPerformanceDOMap = CollectionUtils.convertMap(contractPerformanceDOS, ContractPerformanceDO::getId);
            for (PerfTaskDO taskDO : taskDOS) {
                String alertName = "";
                RiskAlertDO alertDO = new RiskAlertDO();
                alertDO.setPerformTaskId(taskDO.getId());
                ContractPerformanceDO contractPerformance = contractPerformanceDOMap.get(taskDO.getContractPerfId());
                //由履约任务找到合同履约
                if (contractPerformance != null) {

                    alertDO.setContractCode(contractPerformance.getContractCode());
                    alertDO.setContractName(contractPerformance.getContractName());
                    //履约任务的创建者
                    alertDO.setPerformTaskCreator(taskDO.getCreator());

                    alertName = contractPerformance.getContractName() + " - " + taskDO.getName();
                    alertDO.setName(alertName);
                    alertDO.setType(ALERT_SOURCE_PERFORMANCE.getCode());

                }
                riskAlertDOS.add(alertDO);
            }

            riskAlertMapper.insertBatch(riskAlertDOS);

        }

        return "success";
    }

    void emptyTime(@NotNull Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
    }
}
