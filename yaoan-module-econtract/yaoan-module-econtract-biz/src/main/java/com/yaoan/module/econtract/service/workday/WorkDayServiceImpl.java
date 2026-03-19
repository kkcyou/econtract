package com.yaoan.module.econtract.service.workday;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.workday.dto.HolidayData;
import com.yaoan.module.econtract.api.workday.dto.WorkdayResponseDTO;
import com.yaoan.module.econtract.controller.admin.workday.vo.InitReqVO;
import com.yaoan.module.econtract.controller.admin.workday.vo.UpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.workday.WorkdayDO;
import com.yaoan.module.econtract.dal.mysql.workday.WorkDayMapper;
import liquibase.repackaged.org.apache.commons.lang3.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkDayServiceImpl extends ServiceImpl<WorkDayMapper, WorkdayDO> implements WorkDayService{

    @Resource
    private WorkDayMapper workDayMapper;

    @Override
    public PageResult<WorkdayDO> getList(InitReqVO vo) throws Exception {
        LambdaQueryWrapperX<WorkdayDO> queryWrapperX = new LambdaQueryWrapperX();
        queryWrapperX.likeIfPresent(WorkdayDO::getCYear,vo.getYear());
        queryWrapperX.orderByDesc(WorkdayDO::getCDate);
        PageResult<WorkdayDO> pageResult = workDayMapper.selectPage(vo,queryWrapperX);
        return pageResult;
    }

    @Override
    public String initByYear(InitReqVO vo) throws Exception {
        String year = vo.getYear();
        int initYear = Integer.parseInt(year);
        Calendar start = Calendar.getInstance();
        start.set(initYear, 0, 1);
        Calendar end = Calendar.getInstance();
        end.set(initYear+1, 0, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<WorkdayDO> workdayDOList = new ArrayList<>();
        while(start.compareTo(end) <= 0) {
            int w = start.get(Calendar.DAY_OF_WEEK);
            w -=1;
            if(w == 0){
                w= 7;
            }
            String date_type = "0";
            if(w== 6 || w==7){
                date_type = "1";
            }
            String month = start.get(Calendar.MONTH) + 1 + "";
            String day = start.get(Calendar.DATE) + "";
            if(month.length() == 1){
                month = "0" + month;
            }
            if(day.length() == 1){
                day ="0" + day;
            }
            WorkdayDO workdayDO = new WorkdayDO();
            workdayDO.setCYear(start.get(Calendar.YEAR) +"");
            workdayDO.setCMonth(month);
            workdayDO.setCDay(day);
            workdayDO.setDateType(date_type);
            workdayDO.setCWeek(w+"");
            workdayDO.setCDate(format.format(start.getTime()));
            workdayDOList.add(workdayDO);
            //循环，每次天数加1
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
        }
        workDayMapper.insertBatch(workdayDOList);
        return "true";
    }

    @Override
    public String updateDayType(UpdateReqVO vo) throws Exception {
        WorkdayDO workdayDO = workDayMapper.selectById(vo.getId());
        workdayDO.setDateType(vo.getDateType());
        workDayMapper.updateById(workdayDO);
        return workdayDO.getId();
    }

    @Override
    public String getWarningDate(int warningIsWorkday, int beforeDay, int warningDay,LocalDate currentDate)  throws Exception {
        String signDateStr = "";
        //currentDate 提取出来作为参数
        //LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        warningDay -= 1; //当天也算一天，此处减一
        //按工作日进行提醒
        if(warningIsWorkday == 1){
            LambdaQueryWrapperX<WorkdayDO> workdayWrapperx = new LambdaQueryWrapperX();
            workdayWrapperx.eq(WorkdayDO::getDateType,0);
            workdayWrapperx.lt(WorkdayDO::getCDate,currentDate.format(formatter));
            workdayWrapperx.orderByDesc(WorkdayDO::getCDate);
            PageParam page = new PageParam();
            page.setPageSize(warningDay -beforeDay  ).setPageNo(1);

            PageResult<WorkdayDO> pageResult = workDayMapper.selectPage(page,workdayWrapperx);
            List<WorkdayDO> workdayDOList = pageResult.getList();
            if(workdayDOList == null || workdayDOList.size() == 0 ){
                //查不到数据说明表中没有初始化对应的工作日日期
                return signDateStr;
            }
            WorkdayDO workdayDO = workdayDOList.get(workdayDOList.size() -1 );
            signDateStr = workdayDO.getCDate();
        }else{
            int day = warningDay - beforeDay  ;
            LocalDate tenDaysAgo = currentDate.minusDays(day);
            signDateStr = tenDaysAgo.format(formatter);
        }
        return signDateStr;
    }

    @Override
    public String getContractSignEndTime(int warningIsWorkday, int beforeDay, int warningDay, LocalDate currentDate) throws Exception {
        String signDateStr = "";
        //currentDate 提取出来作为参数
        //LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        warningDay -= 1; //当天也算一天，此处减一
        //按工作日进行提醒
        if(warningIsWorkday == 1){
            LambdaQueryWrapperX<WorkdayDO> workdayWrapperx = new LambdaQueryWrapperX();
            workdayWrapperx.eq(WorkdayDO::getDateType,0);
            workdayWrapperx.gt(WorkdayDO::getCDate,currentDate.format(formatter));
            workdayWrapperx.orderByAsc(WorkdayDO::getCDate);
            PageParam page = new PageParam();
            page.setPageSize(warningDay -beforeDay  ).setPageNo(1);

            PageResult<WorkdayDO> pageResult = workDayMapper.selectPage(page,workdayWrapperx);
            List<WorkdayDO> workdayDOList = pageResult.getList();
            if(workdayDOList == null || workdayDOList.size() == 0 ){
                //查不到数据说明表中没有初始化对应的工作日日期
                return signDateStr;
            }
            WorkdayDO workdayDO = workdayDOList.get(workdayDOList.size() -1 );
            signDateStr = workdayDO.getCDate();
        }else{
            int day = warningDay - beforeDay  ;
            LocalDate tenDaysAgo = currentDate.minusDays(day);
            signDateStr = tenDaysAgo.format(formatter);
        }
        return signDateStr;
    }


    @Override
    public String refreshHoliday() throws Exception {
        LocalDateTime localDate = LocalDateTime.now();
        Integer year = localDate.getYear();
        String response = "";
        int maxRetries = 3;
        try {
            response = HttpUtil.get("http://timor.tech/api/holiday/year");
            // 如果请求成功，处理响应
            log.info("获取节假日请求成功，时间:" + System.currentTimeMillis());
        } catch (Exception e) {
            // 记录异常
            // 达到最大重试次数，处理失败逻辑
            log.error("获取节假日请求异常，时间:" + System.currentTimeMillis());
            log.error(e.getMessage());
            return "fail";
//        for (int i = 0; i < maxRetries; i++) {
//            try {
//                response = HttpUtil.get("http://timor.tech/api/holiday/year");
//                // 如果请求成功，处理响应
//                log.info("获取节假日请求成功，时间:" + System.currentTimeMillis());
//                break; // 退出循环
//            } catch (Exception e) {
//                // 记录异常
//                if (i == maxRetries - 1) {
//                    // 达到最大重试次数，处理失败逻辑
//                    log.error("获取节假日请求异常，时间:" + System.currentTimeMillis());
//                    log.error(e.getMessage());
//                    return "fail";
//                }
            // 等待一秒再重试
//                Thread.sleep(1000);
        }
//        }

        WorkdayResponseDTO workdayResponseDTO = JSON.parseObject(response, WorkdayResponseDTO.class);
        if (0 == workdayResponseDTO.getCode()) {
            log.info("【更新节假日信息】 更新时间" );
        }
        LambdaQueryWrapperX<WorkdayDO> wrapperX = new LambdaQueryWrapperX<WorkdayDO>()
                .eq(WorkdayDO::getCYear, String.valueOf(year));
        workDayMapper.delete(wrapperX);

        //初始化节假日
        initByYear(new InitReqVO().setYear(String.valueOf(year)));
        List<WorkdayDO> currentYearDateList = workDayMapper.selectList(WorkdayDO::getCYear, String.valueOf(year));
        if (CollectionUtil.isEmpty(currentYearDateList)) {
            log.error("【节假日数据异常】初始化数据为空");
        }
        //再将拿到的数据导入
        List<Map<String, HolidayData>> infoList = workdayResponseDTO.getHoliday();
        List<HolidayData> dataList = new ArrayList<>();
        for (Map<String, HolidayData> info : infoList) {
            dataList.addAll(info.values());
        }
        Map<String, HolidayData> workdayDataMap = CollectionUtils.convertMap(dataList, HolidayData::getDate);
        if (CollectionUtil.isNotEmpty(dataList)) {
            List<WorkdayDO> updateWorkdayDOs = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (WorkdayDO workdayDO : currentYearDateList) {
                HolidayData data = workdayDataMap.get(workdayDO.getCDate());
                if (ObjectUtil.isNotNull(data)) {
                    String dataStr = data.getDate();
                    LocalDate dataDate = LocalDate.parse(dataStr, formatter);
                    // 获取月份（1-12）
                    int month = dataDate.getMonthValue();
                    int day = dataDate.getDayOfMonth();
                    DayOfWeek week = dataDate.getDayOfWeek();
                    workdayDO.setCMonth(String.valueOf(month));
                    workdayDO.setCDay(String.valueOf(day));
                    workdayDO.setCYear(String.valueOf(year));
                    workdayDO.setCDate(dataStr);

                    workdayDO.setDateType(String.valueOf(data.getHoliday() ? 1 : 0));
                    workdayDO.setCWeek(String.valueOf(week.getValue()));
                    workdayDO.setRemark(data.getName());
                    updateWorkdayDOs.add(workdayDO);
                }

            }
            workDayMapper.updateBatch(updateWorkdayDOs);
        }
        log.info("【更新节假日信息】，更新成功" );
        return "success";
    }


    //计算日期区间内的休息时间
    @Override
    public Date calculateJumpRestDays(Date startDate, int n, int flag){
        if (n == 0){
            Date endDate = DateUtils.addDays(startDate, -n);
            List<WorkdayDO> workdayDOS = workDayMapper.selectList(new LambdaQueryWrapperX<WorkdayDO>().between(WorkdayDO::getCDate, endDate, startDate));
            Map<String, List<WorkdayDO>> dateMaps = workdayDOS.stream().collect(Collectors.groupingBy(WorkdayDO::getCDate));

            int restDays = 0;
            for (int i = 0; i < n; i++) {
                // 判断当前日期是否为周末，如果是周末但存在日期类型为工作日，则继续,否则该日跳过
                WorkdayDO workdayDO = workdayDOS.get(0);
                List<WorkdayDO> workdayDOList = dateMaps.get(workdayDO.getCDate());
                List<String> types = workdayDOList.stream().map(WorkdayDO::getDateType).collect(Collectors.toList());
                if ("6".equals(workdayDO.getCWeek()) || "7".equals(workdayDO.getCWeek())){
                    // 周末。但是存在需要工作的日期规则，则该日为调休的工作日
                    if (types.contains("0")){
                        continue;
                    } else {
                        restDays++;
                    }
                } else {
                    // 不是周末。但存在休息的配置，证明今天是节假日，休息天数加1
                    if (types.contains("1")){
                        restDays++;
                    }
                }
            }
            // 如果最后休息日数量大于0，则继续递归计算
            if (restDays>0){
                return calculateJumpRestDays(endDate, restDays, 0);
            } else {
                return endDate;
            }
        } else {
            Date endDate = DateUtils.addDays(startDate, n);
            List<WorkdayDO> workdayDOS = workDayMapper.selectList(new LambdaQueryWrapperX<WorkdayDO>().between(WorkdayDO::getCDate, startDate, endDate));
            Map<String, List<WorkdayDO>> dateMaps = workdayDOS.stream().collect(Collectors.groupingBy(WorkdayDO::getCDate));

            int restDays = 0;
            for (int i = 0; i < n; i++) {
                // 判断当前日期是否为周末，如果是周末但存在日期类型为工作日，则继续,否则该日跳过
                WorkdayDO workdayDO = workdayDOS.get(0);
                List<WorkdayDO> workdayDOList = dateMaps.get(workdayDO.getCDate());
                List<String> types = workdayDOList.stream().map(WorkdayDO::getDateType).collect(Collectors.toList());
                if ("6".equals(workdayDO.getCWeek()) || "7".equals(workdayDO.getCWeek())){
                    // 周末。但是存在需要工作的日期规则，则该日为调休的工作日
                    if (types.contains("0")){
                        continue;
                    } else {
                        restDays++;
                    }
                } else {
                    // 不是周末。但存在休息的配置，证明今天是节假日，休息天数加1
                    if (types.contains("1")){
                        restDays++;
                    }
                }
            }
            // 如果最后休息日数量大于0，则继续递归计算
            if (restDays>0){
                return calculateJumpRestDays(endDate, restDays,1);
            } else {
                return endDate;
            }
        }

    }
}
