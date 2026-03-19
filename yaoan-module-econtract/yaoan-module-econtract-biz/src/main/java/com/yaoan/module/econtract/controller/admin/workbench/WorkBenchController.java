package com.yaoan.module.econtract.controller.admin.workbench;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.SearchYearReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.fastdraft.FastViaModelRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.ContractAmountStatisticsRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.ContractStatisticCountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.TemplateExpirationReminderRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.WorkbenchStatisticRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskCountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskListRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.trend.WorkBenchSignTrendRespVO;
import com.yaoan.module.econtract.service.workbench.WorkBenchService;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 单位端_工作台
 * 工作台主要是统计付款相关数据的模块
 * @author: Pele
 * @date: 2023/12/28 16:08
 */
@Slf4j
@RestController
@RequestMapping("econtract/agency/workbench")
@Validated
@Tag(name = "单位端_工作台", description = "单位端_工作台")
public class WorkBenchController {

    @Resource
    private WorkBenchService workBenchService;

    /**
     * 单位端_工作台_总统计接口
     */
    @GetMapping("/workbenchStatistic")
    @Operation(summary = "单位端_工作台_总统计接口")
    @Parameter(name = "type", description = "年year,月month，上月premonth")
    public CommonResult<WorkbenchStatisticRespVO> workbenchStatistic(@RequestParam(value = "type", defaultValue = "year") String type) {
        return success(workBenchService.workbenchStatistic(type));
    }

    /**
     * 单位端_工作台_快速起草_模板起草
     */
    @GetMapping("/draftFastViaModel/{size}")
    @Operation(summary = "单位端_工作台_快速起草_模板起草")
    public CommonResult<List<FastViaModelRespVO>> draftFastViaModel(@PathVariable Integer size) {
        return success(workBenchService.draftFastViaModel(size));
    }

    /**
     * 单位端_工作台_待办任务列表
     */
    @PostMapping("/toDoTaskList")
    @Operation(summary = "单位端_工作台_待办任务列表")
    public CommonResult<PageResult<WorkBenchTaskListRespVO>> toDoTaskList(@RequestBody WorkBenchTaskReqVO reqVO) {
        return success(workBenchService.toDoTaskList(reqVO));
    }

    /**
     * 单位端_工作台_统计待办任务数量
     */
    @GetMapping("/countToDoTaskList")
    @Operation(summary = "单位端_工作台_统计待办任务数量")
    public CommonResult<WorkBenchTaskCountRespVO> countToDoTaskList() {
        return success(workBenchService.countToDoTaskList());
    }

    /**
     * 单位端_工作台_合同签订趋势
     *
     * @return {@link CommonResult }<{@link WorkBenchSignTrendRespVO }>
     */
    @GetMapping("/signTrend")
    @Operation(summary = "单位端_工作台_合同签订趋势")
    public CommonResult<WorkBenchSignTrendRespVO> signTrend() {
        return success(workBenchService.signTrend());
    }

    /**
     * 单位端_工作台_合同统计接口
     */
    @GetMapping("/contractStatistics")
    @Operation(summary = "单位端_工作台_合同统计接口")
    public CommonResult<ContractStatisticCountRespVO> contractStatistics(@RequestParam("flag") Integer flag) {
        return success(workBenchService.contractStatistics(flag));
    }
    /**
     * 合同应收/应付情况统计图
     */
    @GetMapping(value = "/contractAmountStatistics")
    @Operation(summary = "合同应收/应付情况统计图")
    public CommonResult<List<ContractAmountStatisticsRespVO>> contractAmountStatistics(@RequestParam("flag") Integer flag) {
        return success(workBenchService.contractAmountStatistics(flag));
    }

    /**
     * 单位端_工作台_模板到期提醒
     */
    @GetMapping(value = "/templateExpirationReminder")
    @Operation(summary = "单位端_工作台_模板到期提醒")
    public CommonResult<List<TemplateExpirationReminderRespVO>> templateExpirationReminder() {
        return success(workBenchService.templateExpirationReminder());
    }


}
