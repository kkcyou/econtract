package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.AcceptanceApplyReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceSaveReqVO;
import com.yaoan.module.econtract.enums.payment.PerformanceAcceptanceEnums;
import com.yaoan.module.econtract.service.contractPerformanceAcceptance.ContractPerformanceAcceptanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 验收")
@RestController
@RequestMapping("/acceptance")
@Validated
public class ContractPerformanceAcceptanceController {

    @Resource
    private ContractPerformanceAcceptanceService contractPerformanceAcceptanceService;

    @PostMapping("/create")
    @Operation(summary = "创建验收")
    public CommonResult<String> createContractPerformanceAcceptance(@Valid @RequestBody ContractPerformanceAcceptanceSaveReqVO createReqVO) {
        return success(contractPerformanceAcceptanceService.createContractPerformanceAcceptance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新验收")
    public CommonResult<Boolean> updateContractPerformanceAcceptance(@Valid @RequestBody ContractPerformanceAcceptanceSaveReqVO updateReqVO) {
        contractPerformanceAcceptanceService.updateContractPerformanceAcceptance(updateReqVO);
        return success(true);
    }

    @PostMapping("/accept")
    @Operation(summary = "验收通过")
    public CommonResult<String> acceptPerformanceAcceptance(@Valid @RequestBody AcceptanceApplyReqVO acceptanceApplyReqVO) {
        acceptanceApplyReqVO.setStatus(PerformanceAcceptanceEnums.DO.getCode());
        return success(contractPerformanceAcceptanceService.acceptancePerformance(acceptanceApplyReqVO));
    }

    @PostMapping("/reject")
    @Operation(summary = "验收不通过")
    public CommonResult<String> rejectPerformanceAcceptance(@Valid @RequestBody AcceptanceApplyReqVO acceptanceApplyReqVO) {
        acceptanceApplyReqVO.setStatus(PerformanceAcceptanceEnums.REJECT.getCode());
        return success(contractPerformanceAcceptanceService.acceptancePerformance(acceptanceApplyReqVO));
    }
    
    @DeleteMapping("/delete")
    @Operation(summary = "删除验收")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteContractPerformanceAcceptance(@RequestParam("id") String id) {
        contractPerformanceAcceptanceService.deleteContractPerformanceAcceptance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得验收")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ContractPerformanceAcceptanceRespVO> getContractPerformanceAcceptance(@RequestParam("id") String id) {
        ContractPerformanceAcceptanceRespVO contractPerformanceAcceptanceRespVO = contractPerformanceAcceptanceService.getContractPerformanceAcceptance(id);
        return success(contractPerformanceAcceptanceRespVO);
    }

    @PostMapping("/page")
    @Operation(summary = "获得验收分页")
    public CommonResult<PageResult<ContractPerformanceAcceptanceRespVO>> getContractPerformanceAcceptancePage(@Valid @RequestBody ContractPerformanceAcceptancePageReqVO pageReqVO) {
        PageResult<ContractPerformanceAcceptanceRespVO> pageResult = contractPerformanceAcceptanceService.getContractPerformanceAcceptancePage(pageReqVO);
        return success(pageResult);
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出验收 Excel")
//    public void exportContractPerformanceAcceptanceExcel(@Valid ContractPerformanceAcceptancePageReqVO pageReqVO,
//              HttpServletResponse response) throws IOException {
//        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
//        List<ContractPerformanceAcceptanceDO> list = contractPerformanceAcceptanceService.getContractPerformanceAcceptancePage(pageReqVO).getList();
//        // 导出 Excel
//        ExcelUtils.write(response, "验收.xls", "数据", ContractPerformanceAcceptanceRespVO.class,
//                        BeanUtils.toBean(list, ContractPerformanceAcceptanceRespVO.class));
//    }

}