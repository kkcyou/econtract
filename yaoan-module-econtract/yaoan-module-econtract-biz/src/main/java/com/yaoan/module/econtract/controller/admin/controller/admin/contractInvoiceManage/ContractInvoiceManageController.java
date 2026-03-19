package com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage;


import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManagePageReqVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageRespVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageSaveReqVO;
import com.yaoan.module.econtract.service.contractInvoiceManage.ContractInvoiceManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 发票")
@RestController
@RequestMapping("/invoicemanage")
@Validated
public class ContractInvoiceManageController {

    @Resource
    private ContractInvoiceManageService contractInvoiceManageService;

    @PostMapping("/create")
    @Operation(summary = "创建发票")
    public CommonResult<String> createContractInvoiceManage(@Valid @RequestBody ContractInvoiceManageSaveReqVO createReqVO) {
        return success(contractInvoiceManageService.createContractInvoiceManage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发票")
    public CommonResult<Boolean> updateContractInvoiceManage(@Valid @RequestBody ContractInvoiceManageSaveReqVO updateReqVO) {
        contractInvoiceManageService.updateContractInvoiceManage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发票")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteContractInvoiceManage(@RequestParam("id") String id) {
        contractInvoiceManageService.deleteContractInvoiceManage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发票")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ContractInvoiceManageRespVO> getContractInvoiceManage(@RequestParam("id") String id) {
        ContractInvoiceManageRespVO contractInvoiceManageRespVO = contractInvoiceManageService.getContractInvoiceManage(id);
        return success(contractInvoiceManageRespVO);
    }

    @PostMapping("/page")
    @Operation(summary = "获得发票分页")
    public CommonResult<PageResult<ContractInvoiceManageRespVO>> getContractInvoiceManagePage(@Valid @RequestBody ContractInvoiceManagePageReqVO pageReqVO) {
        return success(contractInvoiceManageService.getContractInvoiceManagePage(pageReqVO));
       // return success(BeanUtils.toBean(pageResult, ContractInvoiceManageRespVO.class));
    }

   

}