package com.yaoan.module.econtract.controller.admin.performance.contractPerformance;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerformanceRespVO;
import com.yaoan.module.econtract.service.performance.contractPerformance.ContractPerfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同履约
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@RestController
@RequestMapping("econtract/contractPerformance")
@Tag(name = "合同履约", description = "合同履约")
public class ContractPerfController {

    @Resource
    private ContractPerfService contractPerfService;

    /**
     * 查询合同履约列表信息
     * @param contractPerfPageReqVO
     * @return
     */
    @PostMapping(value = "/page/list")
    @Operation(summary = "查询合同履约列表信息", description = "查询合同履约列表信息")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ContractPerformanceRespVO>> queryAllContractPerf(@RequestBody ContractPerfPageReqVO contractPerfPageReqVO) {
        PageResult<ContractPerformanceRespVO> contractPerf = contractPerfService.queryAllContractPerf(contractPerfPageReqVO);
        return success(contractPerf);
    }

    /**
     * 新增合同履约数据
     * @param contractPerfReqVO
     * @return
     */
    @PutMapping(value = "/create")
    @Operation(summary = "新增合同履约数据", description = "新增合同履约数据")
    @OperateLog(logArgs = false)
    public CommonResult<String> createContractPerf(@RequestBody ContractPerfReqVO contractPerfReqVO) {
        String id = contractPerfService.createContractPerf(contractPerfReqVO);
        return success(id);
    }



}
