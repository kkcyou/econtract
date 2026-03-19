package com.yaoan.module.econtract.job.contractrisk;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractPerformanceLogMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contractrisk.ContractRiskMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 合同逾期定时任务
 * @author: Pele
 * @date: 2024/9/26 18:26
 */
@Slf4j
@Component("contractOverdueJob")
public class ContractOverdueJob implements JobHandler {
    @Resource
    private ContractRiskMapper contractRiskMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;

    @Override
    @TenantIgnore
    @Transactional(rollbackFor = Exception.class)
    public String execute(String param) throws Exception {
        log.info("【逾期检测】执行合同逾期定时任务" + System.currentTimeMillis());
        // 找到履约中的合同的逾期未支付的履约计划
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.select4ProcessContract();
        if (CollectionUtil.isEmpty(paymentScheduleDOList)) {
            log.info("【逾期检测】今日未发现逾期履约计划");
            return null;
        }
        // 获得每个合同最早的未支付履约计划
        Map<String, PaymentScheduleDO> earliestScheduleDOMap = paymentScheduleDOList.stream()
                .collect(Collectors.groupingBy(PaymentScheduleDO::getContractId)) // 按 contractId 分组
                .entrySet().stream() // 处理每个分组
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 使用 contractId 作为 Map 的 key
                        entry -> entry.getValue().stream() // 获取该组中的所有元素
                                .min((a, b) -> Integer.compare(a.getSort(), b.getSort())) // 找到 sort 最小的元素
                                .orElse(null) // 如果没有找到，返回 null
                ));
        // 找出过期的计划
        List<String> contractIdList = earliestScheduleDOMap.values()
                .stream()
                .map(PaymentScheduleDO::getContractId) // 获取每个 PaymentScheduleDO 的 contractId
                .collect(Collectors.toList()); // 收集为 List<String>
        List<ContractDO> contractDOList = new ArrayList<ContractDO>();
        for (String id : contractIdList) {
            PaymentScheduleDO paymentScheduleDO = earliestScheduleDOMap.get(id);
            ContractDO contractDO = new ContractDO()
                    .setId(id)
                    .setStatus(ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
                    .setRiskDate(paymentScheduleDO.getPaymentTime());

            contractDOList.add(contractDO);
        }

        // 修改计划对应的合同的状态-变成履约逾期合同
        contractMapper.updateBatch(contractDOList);
        // 新增风险数据
        List<ContractRiskDO> riskDOList = new ArrayList<ContractRiskDO>();
        for (ContractDO contractDO : contractDOList) {
            PaymentScheduleDO paymentScheduleDO = earliestScheduleDOMap.get(contractDO.getId());
            ContractRiskDO contractRiskDO = new ContractRiskDO().setContractId(contractDO.getId()).setRiskType(ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
            contractRiskDO.setDeptId(paymentScheduleDO.getDeptId());
            contractRiskDO.setTenantId(paymentScheduleDO.getTenantId());
            riskDOList.add(contractRiskDO);
        }
        contractRiskMapper.insertBatch(riskDOList);
        log.info("【逾期检测】成功扫描到逾期合同" + contractDOList.size() + "份");

        return "success";
    }
}
