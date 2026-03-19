package com.yaoan.module.econtract.controller.admin.performance.performanceTask;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.*;
import com.yaoan.module.econtract.service.performance.perfTask.PerfTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同履约任务
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@RestController
@RequestMapping("econtract/performanceTask")
@Tag(name = "合同履约任务", description = "合同履约任务")
public class PerfTaskController {

    @Resource
    private PerfTaskService perfTaskService;
    /**
     * 新增合同履约任务
     * @param
     * @return
     */
    @PutMapping(value = "/create")
    @Operation(summary = "新增合同履约任务", description = "新增合同履约任务")
    @OperateLog(logArgs = false)

    public CommonResult<String> createPerfTask(@RequestBody  @Valid PerformanceTaskCreateVO taskCreateVO) {
        String id = perfTaskService.createPerfTask(taskCreateVO);
        return success(id);
    }

    /**
     * 删除合同履约任务
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "删除合同履约任务", description = "删除合同履约任务")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deletePerfTask(@PathVariable String id) {
        perfTaskService.removeById(id);
        return success(true);
    }
    /**
     * 根据合同履约id查看所有合同履约任务信息-下拉框
     * @param id
     * @return
     */
    @GetMapping(value = "/querylist/queryPerfTaskById/{id}")
    @Operation(summary = "根据合同履约id查看所有合同履约任务信息-下拉框", description = "查看所有合同履约任务信息-下拉框")
    @OperateLog(logArgs = false)
    public CommonResult<List<Map<String,Object>>> queryPerfTaskById(@PathVariable String id,String perfTaskId) {
        List<Map<String,Object>> list = perfTaskService.queryPerfTaskById(id,perfTaskId);
        return success(list);
    }

    /**
     * 根据合同履约id查看所有合同履约任务信息-查看合同，编辑履约
     * @param id
     * @return
     */
    @GetMapping(value = "/querylist/queryPerfTaskListById/{id}")
    @Operation(summary = "根据合同履约id查看所有合同履约任务信息-查看合同，编辑履约", description = "根据合同履约id查看所有合同履约任务信息-查看合同，编辑履约")
    @OperateLog(logArgs = false)
    public CommonResult<List<PerformanceTaskInfoRespVO>> queryPerfTaskListById(@PathVariable String id) {
        List<PerformanceTaskInfoRespVO> list = perfTaskService.queryPerfTaskListById(id);
        return success(list);
    }

    /**
     * 根据履约任务id查看合同履约任务详情
     * @param id
     * @return
     */
    @GetMapping(value = "/query/{id}")
    @Operation(summary = "根据履约任务id查看合同履约任务详情", description = "查看合同履约任务详情")
    @OperateLog(logArgs = false)
    public CommonResult<PerformanceTaskInfoRespVO> queryPerfTaskInfoById(@PathVariable String id) {
        PerformanceTaskInfoRespVO map = perfTaskService.queryPerfTaskInfoById(id);
        return success(map);
    }
    /**
     * 根据合同履约id查看所有合同履约任务信息-查看履约,确认履约
     * @param contractPerfPageReqVO
     * @return
     */
    @PostMapping(value = "/querylist/queryContractPerfTaskListById")
    @Operation(summary = "根据合同履约id查看所有合同履约任务信息-查看履约,确认履约", description = "根据合同履约id查看所有合同履约任务信息-查看履约，确认履约")
    @OperateLog(logArgs = false)
    public CommonResult<PerformanceTaskListRespVO> queryContractPerfTaskListById(@RequestBody ContractPerfPageReqVO contractPerfPageReqVO) {
        PerformanceTaskListRespVO performanceTaskListRespVO = perfTaskService.queryContractPerfTaskListById(contractPerfPageReqVO);
        return success(performanceTaskListRespVO);
    }

    /**
     * 查看所有合同履约任务信息-履约监控
     * @param contractPerfPageReqVO
     * @return
     */
    @PostMapping(value = "/querylist/queryContractPerfTaskList")
    @Operation(summary = "查看所有合同履约任务信息-履约监控", description = "查看所有合同履约任务信息-履约监控")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<PerfTaskAndContractListRespVO>> queryContractPerfTaskList(@RequestBody ContractPerfPageReqVO contractPerfPageReqVO) {
        PageResult<PerfTaskAndContractListRespVO> list = perfTaskService.queryContractPerfTaskList(contractPerfPageReqVO);
        return success(list);
    }



    /**
     * 确认履约
     * @param perfTaskReqVO
     * @return
     */
    @PostMapping(value = "/surePerformance")
    @Operation(summary = "确认履约", description = "确认履约")
    @OperateLog(logArgs = false)
    public CommonResult surePerformance(@Valid PerfTaskReqVO perfTaskReqVO) throws IOException {
         perfTaskService.surePerformance(perfTaskReqVO);
        return success(true);
    }
    /**
     * 确认履约V2
     * @param perfTaskReqVO
     * @return
     */
    @PostMapping(value = "/surePerformanceV2")
    @Operation(summary = "确认履约", description = "确认履约")
    @OperateLog(logArgs = false)
    public CommonResult surePerformanceV2(@Valid @RequestBody PerfTaskReqVO perfTaskReqVO){
        perfTaskService.surePerformanceV2(perfTaskReqVO);
        return success(true);
    }

    /**
     * 询履约提醒时间为当日的所有履约任务列表，履约提醒
     * @param
     * @return
     */
    @PostMapping(value = "/querylist/queryPerfRemindTaskList")
    @Operation(summary = "查询履约提醒时间为当日的所有履约任务列表，履约提醒 ", description = "查询履约提醒时间为当日的所有履约任务列表，履约提醒 ")
    @OperateLog(logArgs = false)
    public CommonResult<List<PerfTaskAndContractListRespVO>> queryPerfRemindTaskList( )  {
        List<PerfTaskAndContractListRespVO> respVOS = perfTaskService.queryPerfRemindTaskList();
        return success(respVOS);
    }

    /**
     * 继续履约
     * @param
     * @return
     */
    @PostMapping(value = "/continuePerformance/{id}")
    @Operation(summary = "点击继续履约根据合同履约id修改对应的合同履约状态和任务状态 ", description = "点击继续履约根据合同履约id修改对应的合同履约状态和任务状态 ")
    @OperateLog(logArgs = false)
    public CommonResult continuePerformance( @PathVariable String id)  {
        perfTaskService.continuePerformance(id);
        return success(true);
    }
}
