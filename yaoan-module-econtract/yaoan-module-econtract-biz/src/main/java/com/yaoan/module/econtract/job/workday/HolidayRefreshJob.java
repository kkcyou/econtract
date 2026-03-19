package com.yaoan.module.econtract.job.workday;

import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.service.workday.WorkDayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/18 14:04
 */
@Slf4j
@Component("holidayRefreshJob")
public class HolidayRefreshJob implements JobHandler {
    @Resource
    private WorkDayServiceImpl workDayServiceImpl;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        log.info("执行获取节假日定时任务" );
        return workDayServiceImpl.refreshHoliday();
    }
}
