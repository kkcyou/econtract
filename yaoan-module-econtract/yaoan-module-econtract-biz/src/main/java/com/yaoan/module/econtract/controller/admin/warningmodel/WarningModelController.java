package com.yaoan.module.econtract.controller.admin.warningmodel;

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

import com.yaoan.module.econtract.controller.admin.warningmodel.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;
import com.yaoan.module.econtract.convert.warningmodel.WarningModelConvert;
import com.yaoan.module.econtract.service.warningmodel.WarningModelService;

@Tag(name = "管理后台 - 预警模块来源（new预警）")
@RestController
@RequestMapping("/ecms/warning-model")
@Validated
public class WarningModelController {

    @Resource
    private WarningModelService warningModelService;

    @PostMapping("/create")
    @Operation(summary = "创建预警模块来源（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:create')")
    public CommonResult<String> createWarningModel(@Valid @RequestBody WarningModelCreateReqVO createReqVO) {
        return success(warningModelService.createWarningModel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警模块来源（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:update')")
    public CommonResult<Boolean> updateWarningModel(@Valid @RequestBody WarningModelUpdateReqVO updateReqVO) {
        warningModelService.updateWarningModel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警模块来源（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:delete')")
    public CommonResult<Boolean> deleteWarningModel(@RequestParam("id") String id) {
        warningModelService.deleteWarningModel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警模块来源（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:query')")
    public CommonResult<WarningModelRespVO> getWarningModel(@RequestParam("id") String id) {
        WarningModelDO warningModel = warningModelService.getWarningModel(id);
        return success(WarningModelConvert.INSTANCE.convert(warningModel));
    }
    /**
     * 获得预警模块来源（new预警）列表
     * */
    @GetMapping("/list")
    @Operation(summary = "获得预警模块来源（new预警）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<WarningModelRespVO>> getWarningModelList( ) {
        List<WarningModelDO> list = warningModelService.getWarningModelList( );
        return success(WarningModelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预警模块来源（new预警）分页")
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:query')")
    public CommonResult<PageResult<WarningModelRespVO>> getWarningModelPage(@Valid WarningModelPageReqVO pageVO) {
        PageResult<WarningModelDO> pageResult = warningModelService.getWarningModelPage(pageVO);
        return success(WarningModelConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警模块来源（new预警） Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-model:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningModelExcel(@Valid WarningModelExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningModelDO> list = warningModelService.getWarningModelList(exportReqVO);
        // 导出 Excel
        List<WarningModelExcelVO> datas = WarningModelConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警模块来源（new预警）.xls", "数据", WarningModelExcelVO.class, datas);
    }

}
