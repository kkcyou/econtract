package com.yaoan.module.econtract.controller.admin.paymentPlan;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.*;
import com.yaoan.module.econtract.service.paymentPlan.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/performance")
@Tag(name = "履约看板", description = "履约看板")
public class PerformanceDashboardController {
    @Resource
    private PaymentService paymentService;

    
    
    @PostMapping(value = "/queryPerformanceTopCount")
    @Operation(summary = "履约看板-查询合同数量和待审批数量", description = "履约看板-查询合同数量和待审批数量")
    @OperateLog(logArgs = false)
    public CommonResult<DashboardContractCountRespVO> queryPerformanceTopCount() {
        DashboardContractCountRespVO dashboardContractCountRespVO = paymentService.queryPerformanceTopCount();

        return success(dashboardContractCountRespVO);
    }
    @PostMapping(value = "/queryPerformanceCount")
    @Operation(summary = "履约看板-分类查询履约金额", description = "履约看板-分类查询履约金额")
    @OperateLog(logArgs = false)
    public CommonResult<DashboardPerformanceMoneyRespVO> queryPerformanceMoney(@RequestBody DashboardPerformanceMoneyReqVO dashboardPerformanceMoneyReqVO) {

        return success(paymentService.queryPerformanceMoney(dashboardPerformanceMoneyReqVO));
    }
    @PostMapping(value = "/queryContractStatusCount")
    @Operation(summary = "履约看板-分状态查询合同数量", description = "履约看板-分状态查询合同数量")
    @OperateLog(logArgs = false)
    public CommonResult<DashboardContractStatusCountRespVO> queryContractStatusCount() {
        return success(paymentService.queryContractStatusCount());
    }
    @PostMapping(value = "/queryPerformanceList")
    @Operation(summary = "履约看板-查询近期履约计划日历", description = "履约看板-查询近期履约计划日历")
    @OperateLog(logArgs = false)
    public CommonResult<List<Map<String,Object>>> queryPerformanceList() {

        return success(paymentService.queryPerformanceList());
    }

    @PostMapping(value = "/queryRiskContractList")
    @Operation(summary = "履约看板-查询履约风险排行列表", description = "履约看板-查询履约风险排行列表")
    @OperateLog(logArgs = false)
    public CommonResult<List<DashboardContractRiskRespVO>> queryRiskContractList() {

        return success(paymentService.queryRiskContractList());
    }

    @GetMapping(value = "/queryContractComplate/{year}")
    @Operation(summary = "履约看板-查询履约完成情况列表", description = "履约看板-查询履约完成情况列表")
    @OperateLog(logArgs = false)
    public CommonResult<List<DashboardContractComplateRespVO>> queryContractComplate(@PathVariable("year") String year) {
        return success(paymentService.queryContractComplate(year));
    }
  
}
