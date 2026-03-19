package com.yaoan.module.econtract.controller.admin.contractrisk;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskRespVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;
import com.yaoan.module.econtract.service.contractrisk.ContractRiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 合同风险")
@RestController
@RequestMapping("/contractrisk")
@Validated
public class ContractRiskController {

    @Resource
    private ContractRiskService contractRiskService;

    @PostMapping("/create")
    @Operation(summary = "创建合同风险")
    public CommonResult<String> createContractRisk(@Valid @RequestBody ContractRiskSaveReqVO createReqVO) {
        return success(contractRiskService.createContractRisk(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同风险")
    public CommonResult<Boolean> updateContractRisk(@Valid @RequestBody ContractRiskUpdateReqVO updateReqVO) {
        contractRiskService.updateContractRisk(updateReqVO);
        return success(true);
    }

    @PutMapping("/update4Handle")
    @Operation(summary = "处理合同风险")
    public CommonResult<Boolean> updateContractRisk4Handle(@Valid @RequestBody ContractRiskUpdateReqVO updateReqVO) {
        contractRiskService.updateContractRisk4Handle(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同风险")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteContractRisk(@RequestParam("id") String id) {
        contractRiskService.deleteContractRisk(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同风险")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ContractRiskRespVO> getContractRisk(@RequestParam("id") String id) {
        return success(contractRiskService.getContractRisk(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同风险分页")
    public CommonResult<PageResult<ContractRiskRespVO>> getContractRiskPage(@Valid ContractRiskPageReqVO pageReqVO) {
        PageResult<ContractRiskDO> pageResult = contractRiskService.getContractRiskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ContractRiskRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同风险 Excel")
    public CommonResult<String>  exportContractRiskExcel(@Valid ContractRiskPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractRiskDO> list = contractRiskService.getContractRiskPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "合同风险.xls", "数据", ContractRiskRespVO.class,
                BeanUtils.toBean(list, ContractRiskRespVO.class));
        return success("success");
    }

    @GetMapping("/closePerformance")
    @Operation(summary = "关闭履约")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<String> closePerformance(@RequestParam("id") String id) {
        contractRiskService.closePerformance(id);
        return success("success");
    }

    @GetMapping("/check4completePerformance")
    @Operation(summary = "校验完成履约")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<String>  check4completePerformance(@RequestParam("id") String id) {
        contractRiskService.check4completePerformance(id);
        return success("success");
    }

    @GetMapping("/completePerformance")
    @Operation(summary = "完成履约")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public  CommonResult<String>  completePerformance(@RequestParam("id") String id) {
        contractRiskService.completePerformance(id);
        return success("success");
    }

    /**
     * 风险忽略
     * 确认忽略当前风险，履约状态将变更为【履约中】
     * @param id 合同id
     * @return
     */
    @OperateLog(logArgs = false)
    @GetMapping(value = "ignoreRisk/{id}")
    @Operation(summary = "风险忽略", description = "风险忽略")
    public  CommonResult<String> ignoreRisk(@PathVariable String id)  {
        return success( contractRiskService.ignoreRisk(id));
    }
}