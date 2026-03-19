package com.yaoan.module.econtract.controller.admin.workbenchmanage;

import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.*;
import com.yaoan.module.econtract.convert.workbenchmanage.WorkbenchConvert;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;
import com.yaoan.module.econtract.service.workbenchmanage.WorkbenchService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.pojo.CommonResult;
import static com.yaoan.framework.common.pojo.CommonResult.success;

import com.yaoan.framework.excel.core.util.ExcelUtils;

import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.*;



@Tag(name = "管理后台 - 工作台管理")
@RestController
@RequestMapping("/workbenchmanage")
@Validated
public class WorkbenchController {

    @Resource
    private WorkbenchService workbenchService;

    @PostMapping("/create")
    @Operation(summary = "创建工作台管理")
    public CommonResult<String> createWorkbench(@Valid @RequestBody WorkbenchCreateReqVO createReqVO) {
        return success(workbenchService.createWorkbench(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作台管理")
    public CommonResult<Boolean> updateWorkbench(@Valid @RequestBody WorkbenchUpdateReqVO updateReqVO) {
        workbenchService.updateWorkbench(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作台管理")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteWorkbench(@RequestParam("id") String id) {
        workbenchService.deleteWorkbench(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作台管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WorkbenchRespVO> getWorkbench(@RequestParam("id") String id) {
        WorkbenchDO workbench = workbenchService.getWorkbench(id);
        return success(WorkbenchConvert.INSTANCE.convert(workbench));
    }

    @GetMapping("/list")
    @Operation(summary = "获得工作台管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<WorkbenchRespVO>> getWorkbenchList(@RequestParam("ids") Collection<String> ids) {
        List<WorkbenchDO> list = workbenchService.getWorkbenchList(ids);
        return success(WorkbenchConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作台管理分页")
    public CommonResult<PageResult<WorkbenchRespVO>> getWorkbenchPage(@Valid WorkbenchPageReqVO pageVO) {
        PageResult<WorkbenchDO> pageResult = workbenchService.getWorkbenchPage(pageVO);
        return success(WorkbenchConvert.INSTANCE.convertPage(pageResult));
    }



}
