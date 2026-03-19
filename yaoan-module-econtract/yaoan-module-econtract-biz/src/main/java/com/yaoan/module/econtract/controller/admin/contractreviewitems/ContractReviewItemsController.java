package com.yaoan.module.econtract.controller.admin.contractreviewitems;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ClauseGroupRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsBaseVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsExcelVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsExportReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsListReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsUpdateStatusReqVO;
import com.yaoan.module.econtract.convert.contractreviewitems.ContractReviewItemsConvert;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;
import com.yaoan.module.econtract.service.contractreviewitems.ContractReviewItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;


/**
 * @author wsh
 */
@Tag(name = "管理后台 - 合同审查规则")
@RestController
@RequestMapping("/ecms/contract-review-items")
@Validated
public class ContractReviewItemsController {

    @Resource
    private ContractReviewItemsService contractReviewItemsService;


    @PostMapping("/create")
    @Operation(summary = "创建合同审查规则")
    public CommonResult<String> createContractReviewItems(@Valid @RequestBody ContractReviewItemsBaseVO createReqVO) {
        return success(contractReviewItemsService.createContractReviewItems(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新合同审查规则")
    public CommonResult<Boolean> updateContractReviewItems(@Valid @RequestBody ContractReviewItemsBaseVO updateReqVO) {
        contractReviewItemsService.updateContractReviewItems(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同审查规则")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteContractReviewItems(@RequestParam("id") String id) {
        contractReviewItemsService.deleteContractReviewItems(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同审查规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ContractReviewItemsBaseVO> getContractReviewItems(@RequestParam("id") String id) {
        ContractReviewItemsBaseVO contractReviewItems = contractReviewItemsService.getContractReviewItems(id);
        return success(contractReviewItems);
    }

    @PostMapping("/list")
    @Operation(summary = "获得合同审查规则列表")
    public CommonResult<List<ClauseGroupRespVO>> getContractReviewItemsList(@RequestBody ContractReviewItemsListReqVO vo) {
        List<ClauseGroupRespVO> list = contractReviewItemsService.getContractReviewItemsList(vo);
        return success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "获得合同审查规则分页")
    public CommonResult<PageResult<ContractReviewItemsRespVO>> getContractReviewItemsPage(@RequestBody ContractReviewItemsPageReqVO pageVO) {
        PageResult<ContractReviewItemsRespVO> pageResult = contractReviewItemsService.getContractReviewItemsPage(pageVO);
        return success(pageResult);
    }

    @PostMapping("/getReviewRulePage")
    @Operation(summary = "查看审查清单获得合同审查规则分页")
    public CommonResult<PageResult<ContractReviewItemsRespVO>> getContractReviewItemsPageV2(@RequestBody ContractReviewItemsPageReqVO pageVO) {
        PageResult<ContractReviewItemsRespVO> pageResult = contractReviewItemsService.getContractReviewItemsPageV2(pageVO);
        return success(pageResult);
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "更新合同审查规则状态")
    public CommonResult<Boolean> updateReviewStatus(@Valid @RequestBody ContractReviewItemsUpdateStatusReqVO updateReqVO) {
        contractReviewItemsService.updateReviewStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同审查规则 Excel")
    @OperateLog(type = EXPORT)
    public void exportContractReviewItemsExcel(@Valid ContractReviewItemsExportReqVO exportReqVO,
                                               HttpServletResponse response) throws IOException {
        List<ContractReviewItemsDO> list = contractReviewItemsService.getContractReviewItemsExcelList(exportReqVO);
        // 导出 Excel
        List<ContractReviewItemsExcelVO> datas = ContractReviewItemsConvert.INSTANCE.convertListV2(list);
        ExcelUtils.write(response, "合同审查规则.xls", "数据", ContractReviewItemsExcelVO.class, datas);
    }

}
