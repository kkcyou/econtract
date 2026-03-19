package com.yaoan.module.econtract.controller.admin.contractPerformMonitor;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;
import com.yaoan.module.econtract.service.performmonitor.PerformMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;


/**
 * @description:
 * @author: Pele
 * @date: 2023/8/31 18:28
 */
@Slf4j
@RestController
@RequestMapping("econtract/contractPerformMonitor")
@Validated
@Tag(name = "合同履约监控", description = "合同履约监控接口")
public class ContractPerformMonitorController {
    @Resource
    private PerformMonitorService performMonitorService;

    /**
     * 合同履约统计展示
     */
    @PostMapping("/listPerformTaskStatistic")
    @Operation(summary = "合同履约统计展示")
    @OperateLog(logArgs = false)
    public CommonResult<List<ContractPerformOverviewRespVo>> overviewContractPerform() {
        return success(performMonitorService.overviewContractPerform());
    }

    /**
     * 履约任务类型 统计 展示(饼状图)
     */
    @PostMapping("/performTaskStatistic")
    @Operation(summary = "履约任务类型 统计 展示(饼状图)")
    @OperateLog(logArgs = false)
    public CommonResult<List<PerformTaskStatisticRespVo>> performTaskStatistic() {
        return success(performMonitorService.performTaskStatistic());
    }

    /**
     * 工作台-当日风险提示
     */
    @PostMapping("/listTodayRiskAlert")
    @Operation(summary = "当日风险提示 ")
    @OperateLog(logArgs = false)
    public CommonResult<List<RiskAlertRespVO>> listTodayRiskAlert() {
        return success(performMonitorService.listTodayRiskAlert());
    }

    /**
     * 更多风险提示列表
     */
    @PostMapping("/listRiskAlert")
    @Operation(summary = "更多风险提示列表 ")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<RiskAlertPageRespVO>> listMoreRiskAlert(@RequestBody RiskAlertPageReqVO vo) {
        return success(performMonitorService.listMoreRiskAlert(vo));
    }

    /**
     * 应付款统计 展示
     * 只有支付类，且状态为履约完成的任务才会有实付金额
     */
    @PostMapping("/countPayable")
    @Operation(summary = "应付款统计 展示")
    @OperateLog(logArgs = false)
    CommonResult<List<PayableBigRespVO>> countPayable(@RequestBody PayableReqVo vo) {
        return success(performMonitorService.countPayable(vo));
    }

    /**
     * 导出应付款统计
     */
    @GetMapping("/exportExcel")
    @Operation(summary = "导出应付款统计 Excel")
    @OperateLog(type = EXPORT)
    public void exportExcel(@RequestBody PayableReqVo vo, HttpServletResponse response) throws IOException {
        performMonitorService.exportExcel(vo, response);

    }

    /**
     * 工作台-金额统计 接口
     */
    @PostMapping("/statisticsAmount")
    @Operation(summary = "工作台-金额统计 接口")
    @OperateLog(logArgs = false)
    public CommonResult<StatisticsAmountRespVO> statisticsAmount(@RequestBody PaymentApplicationListBpmReqVO vo) throws Exception {
        return success(performMonitorService.statisticsAmount(vo));
    }
}