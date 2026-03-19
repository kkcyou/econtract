package com.yaoan.module.econtract.controller.admin.bpm.performance;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformancePageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformanceRespVO;
import com.yaoan.module.econtract.service.bpm.performance.BpmPerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @author doujiale
 */
@Slf4j
@RestController
@RequestMapping("econtract/performance")
@Validated
@Tag(name = "履约工作流模块", description = "履约工作流模块操作接口")
public class PerformanceController {

    @Resource
    private BpmPerformanceService performanceService;

    @PostMapping("/page")
    @Operation(summary = "获取审批列表数据")
    public CommonResult<PageResult<PerformanceRespVO>> getApprovePage(@RequestBody PerformancePageReqVO reqVO) {
        return success(performanceService.getApprovePage(reqVO));
    }
}