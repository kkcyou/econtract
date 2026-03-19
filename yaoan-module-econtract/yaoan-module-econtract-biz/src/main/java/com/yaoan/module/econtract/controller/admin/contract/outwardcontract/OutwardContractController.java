package com.yaoan.module.econtract.controller.admin.contract.outwardcontract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.*;
import com.yaoan.module.econtract.convert.outwardcontract.OutwardContractConvert;
import com.yaoan.module.econtract.dal.dataobject.outwardcontract.OutwardContractDO;
import com.yaoan.module.econtract.service.outwardcontract.OutwardContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * @author Pele
 */
@Tag(name = "管理后台 - 对外合同")
@RestController
@RequestMapping("/econtract/outward-contract")
@Validated
public class OutwardContractController {

    @Resource
    private OutwardContractService outwardContractService;

    @PostMapping("/create")
    @Operation(summary = "创建对外合同")
    public CommonResult<String> createOutwardContract(@Valid @RequestBody OutwardContractCreateReqVO createReqVO) {
        return success(outwardContractService.createOutwardContract(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新对外合同")
    public CommonResult<Boolean> updateOutwardContract(@Valid @RequestBody OutwardContractUpdateReqVO updateReqVO) {
        outwardContractService.updateOutwardContract(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除对外合同")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteOutwardContract(@RequestParam("id") String id) {
        outwardContractService.deleteOutwardContract(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得对外合同")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OutwardContractRespVO> getOutwardContract(@RequestParam("id") String id) {
        OutwardContractDO outwardContract = outwardContractService.getOutwardContract(id);
        return success(OutwardContractConvert.INSTANCE.convert(outwardContract));
    }

    @GetMapping("/list")
    @Operation(summary = "获得对外合同列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<OutwardContractRespVO>> getOutwardContractList(@RequestParam("ids") Collection<String> ids) {
        List<OutwardContractDO> list = outwardContractService.getOutwardContractList(ids);
        return success(OutwardContractConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得对外合同分页")
    public CommonResult<PageResult<OutwardContractRespVO>> getOutwardContractPage(@Valid OutwardContractPageReqVO pageVO) {
        PageResult<OutwardContractDO> pageResult = outwardContractService.getOutwardContractPage(pageVO);
        return success(OutwardContractConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出对外合同 Excel")
    @OperateLog(type = EXPORT)
    public void exportOutwardContractExcel(@Valid OutwardContractExportReqVO exportReqVO,
                                           HttpServletResponse response) throws IOException {
        List<OutwardContractDO> list = outwardContractService.getOutwardContractList(exportReqVO);
        // 导出 Excel
        List<OutwardContractExcelVO> datas = OutwardContractConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "对外合同.xls", "数据", OutwardContractExcelVO.class, datas);
    }

}
