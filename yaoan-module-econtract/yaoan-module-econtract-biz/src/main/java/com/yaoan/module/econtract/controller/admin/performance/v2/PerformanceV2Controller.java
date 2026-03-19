package com.yaoan.module.econtract.controller.admin.performance.v2;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.BigContractPerformV2RespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.ContractPerformV2ReqVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.PerformQueryRespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.PerformV2SaveReqVO;
import com.yaoan.module.econtract.service.performance.contractPerformance.ContractPerfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/23 14:13
 */
@Slf4j
@RestController
@RequestMapping("econtract/performanceV2")
@Tag(name = "履约管理模块", description = "履约管理模块")
public class PerformanceV2Controller {
    @Resource
    private ContractPerfService contractPerfService;

    /**
     * 合同履约列表
     */
    @PostMapping(value = "/list")
    @Operation(summary = "查询合同履约列表信息", description = "查询合同履约列表信息")
    @OperateLog(logArgs = false)
    public CommonResult<BigContractPerformV2RespVO> list(@RequestBody ContractPerformV2ReqVO reqVO) {
        return success(contractPerfService.list(reqVO));
    }

    /**
     * 新增履约计划
     */
    @PostMapping(value = "/save")
    @Operation(summary = "新增履约计划", description = "新增履约计划")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@Validated @RequestBody PerformV2SaveReqVO reqVO) {
        return success(contractPerfService.save(reqVO));
    }

    /**
     * 查看履约计划
     */
    @GetMapping(value = "/queryById/{id}")
    @Operation(summary = "查看履约计划", description = "查看履约计划")
    @OperateLog(logArgs = false)
    public CommonResult<PerformQueryRespVO> queryById(@PathVariable String id) {
        return success(contractPerfService.queryById(id));
    }
}
