package com.yaoan.module.econtract.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ContractPaymentJob  implements JobHandler {
    @Resource
    PaymentScheduleMapper paymentScheduleMapper;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.debug("执行了付款提醒定时任务～");
        LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        int day = 5  ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate tenDaysAgo = LocalDate.now().minusDays(day);
        String beforeStr = tenDaysAgo.format(formatter);
        lambdaQueryWrapperX.lt(PaymentScheduleDO::getPaymentTime, beforeStr);
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(lambdaQueryWrapperX);
        List<String> contractId = new ArrayList<>();
        
        System.out.println(paymentScheduleDOS.size());

        return "执行付款提醒定时任务完成";
    }
}
