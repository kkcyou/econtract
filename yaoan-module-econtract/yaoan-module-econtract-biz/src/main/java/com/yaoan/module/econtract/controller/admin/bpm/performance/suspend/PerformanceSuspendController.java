package com.yaoan.module.econtract.controller.admin.bpm.performance.suspend;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmSuspendCreateReqVO;
import com.yaoan.module.econtract.service.bpm.performance.suspend.BpmPerformanceSuspendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @author doujiale
 */
@Slf4j
@RestController
@RequestMapping("econtract/performance/suspend")
@Validated
@Tag(name = "履约工作流合同中止模块", description = "履约工作流合同中止模块")
public class PerformanceSuspendController {

    @Resource
    private BpmPerformanceSuspendService performanceSuspendService;

    @PostMapping("/create")
    @Operation(summary = "创建请求申请")
    public CommonResult<String> createSuspend(@Valid @RequestBody BpmSuspendCreateReqVO suspendCreateReqVO) {
        return success(performanceSuspendService.createSuspend(getLoginUserId(), suspendCreateReqVO));
    }

    @GetMapping("/process")
    @Operation(summary = "审批进程")
    public CommonResult<List<BpmProcessRespVO>> process(@RequestParam("processInstanceId") String processInstanceId) {
        return success(performanceSuspendService.process(processInstanceId));
    }

}