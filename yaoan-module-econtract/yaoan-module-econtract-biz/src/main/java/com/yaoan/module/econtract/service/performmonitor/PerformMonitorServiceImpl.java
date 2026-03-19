package com.yaoan.module.econtract.service.performmonitor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;
import com.yaoan.module.econtract.convert.riskalert.RiskAlertConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import com.yaoan.module.econtract.dal.dataobject.riskalert.RiskAlertDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.performTaskType.PerformTaskTypeMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.riskalert.RiskAlertMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import com.yaoan.module.econtract.enums.RiskAlertEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ContractPerfEnums.OVER_TIME_FINISH;
import static com.yaoan.module.econtract.enums.ContractPerfEnums.PERFORMANCE_FINISH;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;
import static com.yaoan.module.econtract.enums.PerformTaskTypeEnums.PERFORM_TASK_TYPE_PAYMENT;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/5 21:07
 */
@Slf4j
@Service
public class PerformMonitorServiceImpl implements PerformMonitorService {

    static final String FIRST_CHAR = "0";
    static final Integer TWELVE_MONTHS = 12;
    static final Integer ZERO = 0;
    @Resource
    private PerforTaskMapper taskMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private ContractPerforMapper performMapper;
    @Resource
    private PerformTaskTypeMapper taskTypeMapper;
    @Resource
    private RiskAlertMapper riskAlertMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;

    /**
     * 工作台-金额统计
     */
    @Override
    public StatisticsAmountRespVO statisticsAmount(PaymentApplicationListBpmReqVO vo) {
        //算出所有签署完成的合同的金额
        List<ContractDO> contractDOList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getAmount,ContractDO::getId)
                .or()
                .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
                .or()
                .eq(ContractDO::getStatus, ContractStatusEnums.TERMINATE_SIGNIND.getCode())
        );
        Double totalAmount = contractDOList.stream()
                .mapToDouble(ContractDO::getAmount)
                .sum();
        List<String> contractIds=contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
        vo.setContractIds(contractIds);
        BigDecimal bigDecimalAmount = new BigDecimal(totalAmount);
        //计算出所有支付完成的金额
        List<PaymentApplicationDO> paymentApplicationDOList = paymentApplicationMapper.statisticsAmount(vo);
//        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectPayedStatisticsAmountList(vo);
        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(paymentApplicationDOList)) {
            totalPayedAmount = paymentApplicationDOList.stream()
                    .map(PaymentApplicationDO::getPayedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal totalUnpaidAmount = bigDecimalAmount.subtract(totalPayedAmount);
        return new StatisticsAmountRespVO()
                .setTotalAmount(ObjectUtil.isNotNull(totalAmount) ? bigDecimalAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)))
                .setPayedAmount(ObjectUtil.isNotNull(totalPayedAmount) ? totalPayedAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)))
                .setUnpaidAmount(ObjectUtil.isNotNull(totalUnpaidAmount) ? totalUnpaidAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)));
    }

    @Override
    public List<PayableBigRespVO> countPayable(PayableReqVo vo) {
        if (ObjectUtil.isNull(vo.getSearchYear())) {
            //默认显示当年数据
            vo.setSearchYear(DateUtil.thisYear());
        }
        String loginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        int year = vo.getSearchYear();
        LocalDate startDate = Year.of(year).atDay(1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        //1，找出登录人创建的所有履约任务 状态:支付类型，履约时间在所选择年，履约金额，实付金额
        LambdaQueryWrapperX<PerfTaskDO> wrapperX = new LambdaQueryWrapperX<PerfTaskDO>()
                //当前创建者
                .eq(PerfTaskDO::getCreator, loginUserId)
                //类型为支付
                .eq(PerfTaskDO::getPerfTaskTypeId, PERFORM_TASK_TYPE_PAYMENT.getCode())
                //按年份
                .betweenIfPresent(PerfTaskDO::getPerfTime, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));

        List<PerfTaskDO> taskDOS = taskMapper.selectList(wrapperX);

        if (CollectionUtil.isEmpty(taskDOS)) {
            return Collections.emptyList();
        }

        //按照月份分组
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        Map<String, List<PerfTaskDO>> groupedByMonthList = taskDOS.stream()
                .collect(Collectors.groupingBy(task -> task.getPerfTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)));

        //将groupedByMonthList 每组赋值给 bigRespVOS
        List<PayableBigRespVO> bigRespVOS = new ArrayList<PayableBigRespVO>();
        for (Map.Entry<String, List<PerfTaskDO>> entry : groupedByMonthList.entrySet()) {

            PayableBigRespVO bigRespVO = new PayableBigRespVO();

            //计算金额
            List<PerfTaskDO> taskDOList = entry.getValue();
            if (CollectionUtil.isEmpty(taskDOList)) {
                throw exception(EMPTY_DATA_ERROR);
            }
            int sumPerformMoneyCount = 0;
            int sumActualMoneyCount = 0;
            for (PerfTaskDO task : taskDOList) {
                if (Objects.equals(PERFORMANCE_FINISH.getCode(), task.getTaskStatus()) || Objects.equals(OVER_TIME_FINISH.getCode(), task.getTaskStatus())) {
                    //统计实付金额
                    if (task.getNumber() != null) {
                        sumActualMoneyCount = sumActualMoneyCount + task.getNumber();
                        // 当月的履约金额
                        sumPerformMoneyCount = sumPerformMoneyCount + task.getNumber();
                    }
                }

            }
            //赋值
            String monthIndex = entry.getKey();
            bigRespVO.setMonthIndex(monthIndex);
            bigRespVO.setActualMoney(sumActualMoneyCount);
            bigRespVO.setPerformMoney(sumPerformMoneyCount);
            bigRespVO.setMonthName(getMonthName(monthIndex));

            bigRespVOS.add(bigRespVO);
        }
        //填补空白月份和空白金额
        fullMonths(bigRespVOS);
        Collections.sort(bigRespVOS, Comparator.comparing(PayableBigRespVO::getMonthIndex));
        return bigRespVOS;
    }

    private void fullMonths(List<PayableBigRespVO> bigRespVOS) {
        Map<String, PayableBigRespVO> voMap = CollectionUtils.convertMap(bigRespVOS, PayableBigRespVO::getMonthIndex);
        List<String> indexs = bigRespVOS.stream().map(PayableBigRespVO::getMonthIndex).collect(Collectors.toList());
        List<String> cleanedIndexs = indexs.stream()
                .map(this::cleanZero)
                .collect(Collectors.toList());
        for (int i = 1; i <= TWELVE_MONTHS; i++) {

            //当月无任务，就设为空值
            if (!cleanedIndexs.contains(String.valueOf(i))) {
                String index = addZero(i);
                String monthIndex = String.valueOf(i);
                PayableBigRespVO bigRespVO = new PayableBigRespVO();
                bigRespVO.setMonthIndex(index);
                bigRespVO.setMonthName(getMonthName(monthIndex));
                bigRespVO.setPerformMoney(0);
                bigRespVO.setActualMoney(0);
                bigRespVOS.add(bigRespVO);
            } else {
                String index = addZero(i);
                PayableBigRespVO bigRespVO = voMap.get(index);
                //若履约金额为空
                if (ObjectUtil.isNull(bigRespVO.getPerformMoney())) {
                    bigRespVO.setPerformMoney(0);
                    bigRespVO.setActualMoney(0);
                }
                //若实付金额为空
                if (ObjectUtil.isNull(bigRespVO.getActualMoney())) {
                    bigRespVO.setActualMoney(0);
                }

            }

        }

    }

    /**
     * 将单位数前面拼 “0”
     */
    public String addZero(int index) {
        String iAsString;
        if (index < 10) {
            iAsString = "0" + index;
        } else {
            iAsString = String.valueOf(index);
        }
        return iAsString;
    }

    @Override
    public List<RiskAlertRespVO> listTodayRiskAlert() {
        // 获取昨天的日期
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.yesterday());

        //找出昨天的超期履约的履约任务:履约时间是昨天，状态为履约超期
        LambdaQueryWrapperX<PerfTaskDO> wrapperX = new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getTaskStatus, PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode()).eq(PerfTaskDO::getPerfTime, yesterday);
        List<PerfTaskDO> taskDOS = taskMapper.selectList(wrapperX);
        if (CollectionUtil.isEmpty(taskDOS)) {
            return Collections.emptyList();
        }
        List<RiskAlertRespVO> respVOS = convert2ReVo(taskDOS);
        return respVOS;
    }

    @Override
    public PageResult<RiskAlertPageRespVO> listMoreRiskAlert(RiskAlertPageReqVO vo) {
        LambdaQueryWrapperX<RiskAlertDO> wrapperX = new LambdaQueryWrapperX<RiskAlertDO>()
                .orderByDesc(RiskAlertDO::getCreateTime)
                .betweenIfPresent(RiskAlertDO::getCreateTime, vo.getStartTime(), vo.getEndTime());
        if (StrUtil.isNotBlank(vo.getSearchText())) {
            wrapperX.or().like(RiskAlertDO::getContractCode, vo.getSearchText())
                    .or().like(RiskAlertDO::getContractName, vo.getSearchText());
            //目前不能查询相对方
        }
        PageResult<RiskAlertDO> pageResult = riskAlertMapper.selectPage(vo, wrapperX);
        if (CollectionUtil.isEmpty(pageResult.getList())) {
            return new PageResult<RiskAlertPageRespVO>();
        }
        PageResult<RiskAlertPageRespVO> result = RiskAlertConverter.INSTANCE.convert2Resp(pageResult);
        result.getList().forEach(rs -> {
            RiskAlertEnums enums = RiskAlertEnums.getInstance(rs.getType());
            if (enums != null) {
                rs.setTypeStr(enums.getInfo());
            }
        });
        return result;
    }

    @Override
    public void exportExcel(PayableReqVo vo, HttpServletResponse response) throws IOException {
        if (ObjectUtil.isNull(vo.getSearchYear())) {
            vo.setSearchYear(DateUtil.thisYear());
        }
        //得到时间
        int year = vo.getSearchYear();
        LocalDate startTime = LocalDate.ofYearDay(year, 1);
        LocalDate endTime = LocalDate.ofYearDay(year, startTime.lengthOfYear());

        Date sTime = Date.from(startTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date eTime = Date.from(endTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        //得到表头
        String[] headers = {"序号", "月份", "实付金额（元）", "履约金额（元）"};

        List<PayableBigRespVO> list = countPayable(vo);
        //得到数据内容
        List<PayableExcelVO> datas = convert2Excel(list);
//        ExcelUtils.write(response, "应付款统计表.xls", "应付款统计sheet", PayableExcelVO.class, datas);

        //导出样式表格
        export(response, datas, "应付款统计表", "应付款统计表sheet", headers, sTime, eTime);
    }

    /**
     * 导出电子文件安全检测情况分析柱形图数据
     */
    private void export(HttpServletResponse response, List<PayableExcelVO> dataList, String fileName, String sheetName, String[] headers, Date startExecutTime, Date endExecutTime) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 创建一个新的工作簿
        Sheet sheet = workbook.createSheet("Sheet1"); // 创建一个名为 "Sheet1" 的工作表
        // 设置 内容行居中
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置边框
        centerStyle.setBorderBottom(BorderStyle.THIN);
        centerStyle.setBorderLeft(BorderStyle.THIN);
        centerStyle.setBorderRight(BorderStyle.THIN);
        centerStyle.setBorderTop(BorderStyle.THIN);
        // 设置背景颜色为白色
        centerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        centerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //第一行标题样式
        CellStyle style3 = workbook.createCellStyle();
        Font font3 = workbook.createFont();
        font3.setFontHeightInPoints((short) 14); // 设置字体大小为14
        font3.setBold(true); // 设置字体加粗
        style3.setFont(font3);
        style3.setAlignment(HorizontalAlignment.CENTER);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置背景颜色为白色
        style3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        CellStyle styleRight = workbook.createCellStyle();
        styleRight.setBorderRight(BorderStyle.THIN);
        CellStyle styleLeft = workbook.createCellStyle();
        styleLeft.setBorderLeft(BorderStyle.THIN);
        CellStyle styleTop = workbook.createCellStyle();
        styleTop.setBorderTop(BorderStyle.THIN);
        CellStyle styleTopRight = workbook.createCellStyle();
        styleTopRight.setBorderTop(BorderStyle.THIN);
        styleTopRight.setBorderRight(BorderStyle.THIN);
        // 创建第一行
        Row row1 = sheet.createRow(0);
        Cell celltop = row1.createCell(0);
        sheet.autoSizeColumn(0, true); // 自动调整列宽
        celltop.setCellValue("应付款统计");
        celltop.setCellStyle(style3);
        //合并第一行
        CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, 3);
        //添加要合并地址到表格
        sheet.addMergedRegion(rangeAddress);
        Cell celltop1 = row1.createCell(1);
        celltop1.setCellStyle(styleTop);
        Cell celltop2 = row1.createCell(2);
        celltop2.setCellStyle(styleTop);
        Cell celltop3 = row1.createCell(3);
        celltop3.setCellStyle(styleTopRight);
//        Cell celltop4 = row1.createCell(4);
//        celltop4.setCellStyle(styleTop);
//        Cell celltop6 = row1.createCell(5);
//        celltop6.setCellStyle(styleTopr);

        // 设置第二行样式
        CellStyle mergedCellStyle = workbook.createCellStyle();
        mergedCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        mergedCellStyle.setAlignment(HorizontalAlignment.LEFT);
        mergedCellStyle.setIndention((short) 15);
        // 设置第二行字体
        Font mergedFont = workbook.createFont();
        mergedFont.setFontHeightInPoints((short) 9);
        mergedCellStyle.setFont(mergedFont);

        // 设置背景颜色为白色
        mergedCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        mergedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        mergedCellStyle.setBorderLeft(BorderStyle.THIN);
        //创建第二行
        Row row2 = sheet.createRow(1);
        Cell celldate = row2.createCell(0);
        String date = getstrTOdate2(new Date());
        String startTime = getstrTOdate(startExecutTime);
        String endTime = getstrTOdate(endExecutTime);
        celldate.setCellValue("数据周期：" + startTime + "至" + endTime + System.lineSeparator() + "  导出时间：" + date);
        //合并第二行
        CellRangeAddress rangeAddressDATE = (new CellRangeAddress(1, 1, 0, 3));
        sheet.addMergedRegion(rangeAddressDATE);
        celldate.setCellStyle(mergedCellStyle);
        Cell celldate6 = row2.createCell(3);
        celldate6.setCellStyle(styleRight);


        // 创建字段名样式
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建字段名行
        Row headerRow = sheet.createRow(2);

        // 设置字段名及样式
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            sheet.autoSizeColumn(i);
            cell.setCellStyle(style);

        }
        // 创建数据行
        for (int i = 0; i < dataList.size(); i++) {//行

            Row dataRow = sheet.createRow(i + 3);
            PayableExcelVO data = dataList.get(i);
            // 设置数据
            int actualMoney = data.getActualMoney();
            int performMoney = data.getPerformMoney();
//            int fail = fileTotalNum - filePassNum;
            //序号
            Cell cell1 = dataRow.createCell(0);
            cell1.setCellValue(i + 1);
            //月份
            Cell cell2 = dataRow.createCell(1);
            cell2.setCellValue(data.getMonthName());
            //实付金额
            Cell cell3 = dataRow.createCell(2);
            cell3.setCellValue(actualMoney);
            Cell cell4 = dataRow.createCell(3);
            cell4.setCellValue(performMoney);
//            Cell cell5 = dataRow.createCell(4);
//            cell5.setCellValue(fail);
//            Cell cell6 = dataRow.createCell(5);
//            cell6.setCellValue(data.get("filePassFreq").toString());
        }
        // 遍历表格，将每个单元格的样式设置为居中
        for (Row row : sheet) {
            //-1 是自行调整的意思
            row.setHeight((short) -1);
            if (row.getRowNum() >= 2) {
                for (Cell cell : row) {
                    cell.setCellStyle(centerStyle);
                }
            }
        }
        // 设置单元格的高度
        row1.setHeight((short) 1000);

        //设置行宽
        sheet.setColumnWidth(0, 10 * 256); // 设置第一列的宽度为10个字符宽度
        sheet.setColumnWidth(1, 10 * 256); // 设置第二列的宽度为10个字符宽度
        sheet.setColumnWidth(2, 20 * 256); // 设置第三列的宽度为10个字符宽度
        sheet.setColumnWidth(3, 42 * 256); // 设置第四列的宽度为50个字符宽度
        // 将工作簿写入输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        // 将输出流转换为字节数组
        byte[] bytes = bos.toByteArray();
        // 设置字符编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();

    }

    /**
     * 将date数据转换成yyyy-MM-dd 类型字符串
     */
    private String getstrTOdate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    /**
     * 将date数据转换成yyyy-MM-dd dd:mm:ss类型字符串
     */
    private String getstrTOdate2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd dd:mm:ss");
        return formatter.format(date);
    }

    private List<PayableExcelVO> convert2Excel(List<PayableBigRespVO> list) {
        List<PayableExcelVO> excelVOS = new ArrayList<PayableExcelVO>();
        for (PayableBigRespVO vo : list) {
            PayableExcelVO excelVO = new PayableExcelVO();
            excelVO.setMonthIndex(vo.getMonthIndex());
            excelVO.setMonthName(vo.getMonthName());
            excelVO.setPerformMoney(vo.getPerformMoney());
            excelVO.setActualMoney(vo.getActualMoney());
            excelVOS.add(excelVO);
        }
        return excelVOS;
    }

    @Override
    public List<ContractPerformOverviewRespVo> overviewContractPerform() {
        // 集中
        List<ContractPerformOverviewRespVo> list = new ArrayList<>();
        ContractPerformOverviewRespVo vo = new ContractPerformOverviewRespVo();

        //1,将所有合同履约找出
        List<ContractPerformanceDO> sumList = performMapper.selectList();

        //2,根据状态赋值

        // 履约中
        int inPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.IN_PERFORMANCE.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, inPerformCount);
        vo.setName("履约中");
        vo.setCode(1);
        list.add(vo);

        // 履约暂停
        int pausePerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.PERFORMANCE_PAUSE.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, pausePerformCount);
        vo.setName("履约暂停");
        vo.setCode(2);
        list.add(vo);

        // 履约结束
        int endPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.PERFORMANCE_END.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, endPerformCount);
        vo.setName("履约结束");
        vo.setCode(3);
        list.add(vo);

        // 履约完成
        int finishPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(PERFORMANCE_FINISH.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, finishPerformCount);
        vo.setName("履约完成");
        vo.setCode(4);
        list.add(vo);

        // 履约超期
        int overTimeInPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.PERFORMANCE_OVER_TIME.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, overTimeInPerformCount);
        vo.setName("履约超期");
        vo.setCode(5);
        list.add(vo);

        // 超期暂停
        int overTimePausePerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.OVER_TIME_PAUSE.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, overTimePausePerformCount);
        vo.setName("超期暂停");
        vo.setCode(6);
        list.add(vo);

        // 超期结束
        int overTimeEndPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(ContractPerfEnums.OVER_TIME_END.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, overTimeEndPerformCount);
        vo.setName("超期结束");
        vo.setCode(7);
        list.add(vo);

        // 超期完成
        int overTimeFinishPerformCount = sumList.stream().filter(contract -> contract.getContractStatus().equals(OVER_TIME_FINISH.getCode())).collect(Collectors.toList()).size();
        vo = getNewVo(vo, overTimeFinishPerformCount);
        vo.setName("超期完成");
        vo.setCode(8);
        list.add(vo);

        return list;
    }

    /**
     * 履约任务类型 统计 展示(饼状图)
     */
    @Override
    public List<PerformTaskStatisticRespVo> performTaskStatistic() {
        List<PerformTaskStatisticRespVo> resultList = new ArrayList<PerformTaskStatisticRespVo>();

        //1,找出所有履约任务
        List<PerfTaskDO> taskDOS = perforTaskMapper.selectList();
        if (CollectionUtil.isEmpty(taskDOS)) {
            return Collections.emptyList();
        }
        //统计所有存在的类型ID
        List<PerformTaskTypeDO> types = taskTypeMapper.selectList();
        Map<String, PerformTaskTypeDO> typeMap = CollectionUtils.convertMap(types, PerformTaskTypeDO::getId);

        //分组
        Map<String, List<PerfTaskDO>> groupedByType = taskDOS.stream()
                .collect(Collectors.groupingBy(PerfTaskDO::getPerfTaskTypeId));

        for (Map.Entry<String, List<PerfTaskDO>> entry : groupedByType.entrySet()) {
            PerformTaskStatisticRespVo vo = new PerformTaskStatisticRespVo();
            String type = "";
            if (typeMap.get(entry.getKey()) != null) {
                type = typeMap.get(entry.getKey()).getName() + "类型";

            }
            int count = entry.getValue().size();
            vo.setName(type);
            vo.setValue(count);
            resultList.add(vo);
        }

        return resultList;
    }

    /**
     * 转换
     */
    public ContractPerformOverviewRespVo getNewVo(ContractPerformOverviewRespVo vo, int number) {
        ContractPerformOverviewRespVo newVo = new ContractPerformOverviewRespVo();
        newVo.setNumber(number);
        return newVo;
    }

    public PerformTaskStatisticRespVo getNewVo(PerformTaskStatisticRespVo vo, int number, int sumNumber) {
        PerformTaskStatisticRespVo newVo = new PerformTaskStatisticRespVo();
        double percentage = (double) number / sumNumber * 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        newVo.setValue(number);
        return newVo;
    }

    /**
     * List<PerfTaskDO>转成 List<RiskAlertRespVO>
     */
    List<RiskAlertRespVO> convert2ReVo(List<PerfTaskDO> dos) {
        List<RiskAlertRespVO> resultList = new ArrayList<RiskAlertRespVO>();
        dos.forEach(taskDO -> {
            RiskAlertRespVO vo = new RiskAlertRespVO();
            vo.setPerformTime(taskDO.getPerfTime());
            PerfTaskDO perfTaskDO = perforTaskMapper.selectById(taskDO.getId());
            vo.setRiskReason("履约超期的内容：" + perfTaskDO.getContent());
            vo.setRiskResource(RiskAlertEnums.ALERT_SOURCE_PERFORMANCE.getInfo());
            resultList.add(vo);
        });
        return resultList;
    }

    /**
     * List<PerfTaskDO>转成 List<PayableRespVo>
     */
    List<PayableRespVo> convert2PayVo(List<PerfTaskDO> dos) {
        List<PayableRespVo> resultList = new ArrayList<PayableRespVo>();
        dos.forEach(taskDO -> {
            PayableRespVo vo = new PayableRespVo();
            vo.setPerformDate(taskDO.getPerfTime());
            vo.setPayAmount(taskDO.getNumber());
            resultList.add(vo);
        });
        return resultList;
    }

    /**
     * 自动转成月份名称
     */
    public String getMonthName(String m) {
        if (m.startsWith(FIRST_CHAR)) {
            m = m.substring(1);
        }
        return m + "月";
    }

    /**
     * monthIndex去零
     */
    public String cleanZero(String m) {
        if (m.startsWith(FIRST_CHAR)) {
            m = m.substring(1);
        }
        return m;
    }


}
