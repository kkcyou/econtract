package com.yaoan.module.econtract.controller.admin.contractarchives;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.*;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.service.contractarchives.ContractArchivesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

/**
 * 该控制器负责归档管理
 */
@Tag(name = "管理后台 - 合同档案")
@RestController
@RequestMapping("/ecms/contract-archives")
@Validated
public class ContractArchivesController {

    @Resource
    private ContractArchivesService contractArchivesService;

    @PostMapping("/create")
    @Operation(summary = "创建合同档案")
    public CommonResult<String> createContractArchives(@Valid @RequestBody ContractArchivesSaveReqVO createReqVO) {
        return success(contractArchivesService.createContractArchives(createReqVO));
    }

    //增加补充档案接口（修改档案表数据、修改附件表数据）
    @PostMapping("/supplement")
    @Operation(summary = "补充合同档案")
    public CommonResult<String> supplementContractArchives(@Valid @RequestBody ContractArchivesSupplementReqVO createReqVO) {
        return success(contractArchivesService.supplementContractArchives(createReqVO));
    }
//
//    @PutMapping("/update")
//    @Operation(summary = "更新合同档案")
//    @PreAuthorize("@ss.hasPermission('ecms:contract-archives:update')")
//    public CommonResult<Boolean> updateContractArchives(@Valid @RequestBody ContractArchivesSaveReqVO updateReqVO) {
//        contractArchivesService.updateContractArchives(updateReqVO);
//        return success(true);
//    }
//
//    @DeleteMapping("/delete")
//    @Operation(summary = "删除合同档案")
//    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('ecms:contract-archives:delete')")
//    public CommonResult<Boolean> deleteContractArchives(@RequestParam("id") String id) {
//        contractArchivesService.deleteContractArchives(id);
//        return success(true);
//    }

    @PostMapping("/get")
    @Operation(summary = "获得合同档案")
    public CommonResult<ContractArchivesRespVO> getContractArchives(@RequestBody ContractArchivesReqVO vo) throws Exception {
        ContractArchivesRespVO result = contractArchivesService.getContractArchives(vo);
        return success(result);
    }


    //档案库列表查询，查询条件为已归档、、待审批
    @PostMapping("/page")
    @Operation(summary = "获得档案检索和借阅申请分页")
    public CommonResult<PageResult<ContractArchivesRespVO>> getContractArchivesPage(@Valid @RequestBody ContractArchivesPageReqVO pageReqVO) {
        PageResult<ContractArchivesRespVO> pageResult = contractArchivesService.getContractArchivesPage(pageReqVO);
        return success(pageResult);
    }

    @PostMapping("/archivePage")
    @Operation(summary = "获得档案库分页")
    public CommonResult<PageResult<ContractArchivesRespVO>> archivePage(@Valid @RequestBody ContractArchivesPageReqVO pageReqVO) {
        PageResult<ContractArchivesRespVO> pageResult = contractArchivesService.archivePage(pageReqVO);
        return success(pageResult);
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出合同档案 Excel")
    public void exportContractArchivesExcel(@Valid ContractArchivesPageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractArchivesRespVO> list = contractArchivesService.getContractArchivesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "合同档案.xls", "数据", ContractArchivesRespVO.class,
               list);
    }

}